package by.my.tools

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import by.my.tools.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CodeScanner {
    private lateinit var binding: ActivityMainBinding
    private val webView
        get() = binding.webView
    private val fab
        get() = binding.floatingActionButton

    private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCameraFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareWebView(savedInstanceState)
    }

    private fun prepareWebView(savedInstanceState: Bundle?) {
        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    CookieManager.getInstance().flush()
                }
            }
            settings.javaScriptEnabled = true
            addJavascriptInterface(this@MainActivity as CodeScanner, "NativeScanner")
            if (savedInstanceState != null) {
                restoreState(savedInstanceState)
            } else {
                loadUrl(MY_TOOLS_MAIN_URL)
            }
        }
        fab.setOnClickListener {
            openCameraFragment()
        }
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun scanBarCode(fieldName: String) {
        //TODO: Check camera permission and open camera fragment
    }

    override fun scanQRCode(fieldName: String) {
        //TODO: Check camera permission and open camera fragment
    }

    private fun openCameraFragment() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsLauncher.launch(Manifest.permission.CAMERA)
            return
        }

        val cameraFragment = CameraFragment()
        cameraFragment.show(supportFragmentManager, "aa")
    }

    companion object {
        const val MY_TOOLS_MAIN_URL = "https://my-tools.by"
    }
}