package by.my.tools

import android.media.ImageReader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import by.my.tools.databinding.FragmentCameraBinding
import com.google.common.util.concurrent.ListenableFuture

class CameraFragment : DialogFragment() {

    private var binding: FragmentCameraBinding? = null
    private val previewView: PreviewView
        get() = binding!!.previewView
    private var imageReader: ImageReader? = null

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        requestCameraOpen()
    }

    override fun onPause() {
        super.onPause()

    }

    private fun requestCameraOpen() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext()).apply {
            addListener(
                {
                    val cameraProvider = cameraProviderFuture!!.get()
                    cameraProvider.unbindAll()
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    cameraProvider.bindToLifecycle(
                        this@CameraFragment,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                },
                ContextCompat.getMainExecutor(requireContext())
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun releaseCameraAndPreview() {

    }


}