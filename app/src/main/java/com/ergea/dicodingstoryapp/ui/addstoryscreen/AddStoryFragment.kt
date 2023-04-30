package com.ergea.dicodingstoryapp.ui.addstoryscreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.ergea.dicodingstoryapp.R
import com.ergea.dicodingstoryapp.databinding.FragmentAddStoryBinding
import com.ergea.dicodingstoryapp.utils.*
import com.ergea.dicodingstoryapp.wrapper.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddStoryViewModel by viewModels()
    private var getFile: File? = null
    private lateinit var pathPhoto: String


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val file = File(pathPhoto).also { file -> getFile = file }
            val os: OutputStream

            // Rotate image to correct orientation
            val bitmap = BitmapFactory.decodeFile(getFile?.path)
            val exif = ExifInterface(pathPhoto)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap: Bitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }

            // Convert rotated image to file
            try {
                os = FileOutputStream(file)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()

                getFile = file
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.image.setImageBitmap(rotatedBitmap)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile
            binding.image.setImageURI(selectedImg)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back()
        chooseImageUpload()
        uploadStory()
    }

    private fun observeData() {
        viewModel.addStory.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.animationLoading.isVisible = true
                }
                is Resource.Error -> {
                    binding.animationLoading.isVisible = false
                    view?.let { view ->
                        showSnackBar(view, it.message.toString())
                    }
                }
                is Resource.Success -> {
                    binding.animationLoading.isVisible = false
                    view?.let { view ->
                        showSnackBar(view, it.data?.message.toString())
                    }
                    findNavController().navigate(R.id.homeFragment)
                }
                is Resource.Empty -> {
                    binding.animationLoading.isVisible = false
                }
            }
        }
    }

    private fun chooseImageUpload() {
        binding.apply {
            btnCamera.setOnClickListener { launchCamera() }
            btnGallery.setOnClickListener { launchGallery() }
        }
    }

    private fun uploadStory() {
        binding.btnUpload.setOnClickListener {
            if (validateInput()) {
                val description = binding.etDescription.text.toString().trim()
                getFile?.let { getFile -> imageMultipart(getFile) }?.let { fileMultipart ->
                    addStory(fileMultipart, description)
                }
            }
        }
        binding.btnUploadWithLocation.setOnClickListener {
            if (validateInput()) {
                val permissionCheck =
                    checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    val locationManager =
                        requireActivity().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val location: Location? =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val description = binding.etDescription.text.toString().trim()
                    getFile?.let { getFile -> imageMultipart(getFile) }?.let { fileMultipart ->
                        addStory(
                            fileMultipart,
                            description,
                            location?.latitude?.toFloat(),
                            location?.longitude?.toFloat()
                        )
                    }
                } else {
                    requestLocationPermission()
                }
            }
        }
    }

    private fun addStory(
        photo: MultipartBody.Part,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ) {
        viewModel.addNewStory(photo, stringToRequestBody(description), lat, lon)
        observeData()
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 201)
    }

    private fun validateInput(): Boolean {
        var flag = true
        binding.apply {
            if (getFile == null) {
                flag = false
                view?.let {
                    showSnackBar(it, "Upload your photo first!")
                }
            } else if (etDescription.text.toString().isEmpty()) {
                flag = false
                tilDescription.error = getString(R.string.description_empty)
                etDescription.requestFocus()
            }
        }
        return flag
    }


    private fun back() {
        binding.icBackBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun launchGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                Constants.MY_PACKAGE,
                it
            )
            pathPhoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}