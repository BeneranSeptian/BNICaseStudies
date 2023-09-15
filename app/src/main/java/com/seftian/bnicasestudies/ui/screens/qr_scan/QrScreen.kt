package com.seftian.bnicasestudies.ui.screens.qr_scan

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.common.util.concurrent.ListenableFuture
import com.seftian.bnicasestudies.ui.Routes
import com.seftian.bnicasestudies.util.BarcodeAnalyzer
import com.seftian.bnicasestudies.util.Helper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun QrScreen(
    navController: NavController
) {
    CameraPreview(toDetail = {
        navController.navigate(Routes.PaymentDetail.withIdQr(it)){
            popUpTo(Routes.QrScreen.route) { inclusive = true }
        }
    })
}

@Composable
fun CameraPreview(
    toDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null


    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            preview?.let {
                cameraProviderFuture?.get()?.unbind(it)
            }
        }
    }

    Column {
        AndroidView(
            factory = { AndroidViewContext ->
                PreviewView(AndroidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.weight(0.8f),
            update = { previewView ->

                val cameraSelector: CameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture?.addListener({
                    val cameraProvider: ProcessCameraProvider =
                        cameraProviderFuture?.get() ?: return@addListener
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val barcodeAnalyzer = BarcodeAnalyzer { barcodes ->
                        barcodes.forEach { barcode ->
                            barcode.rawValue?.let { barcodeValue ->

                                if(!Helper.isQrStringValid(barcodeValue)){
                                    Toast.makeText(context, "Kode tidak valid", Toast.LENGTH_SHORT).show()
                                    return@let
                                }

                                barCodeVal.value = barcodeValue
                                toDetail(barcodeValue)
                            }
                        }
                    }
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyzer)
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
    }
}