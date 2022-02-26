package by.my.tools

import android.webkit.JavascriptInterface

interface CodeScanner {
    @JavascriptInterface
    fun scanBarCode(fieldName: String)
    @JavascriptInterface
    fun scanQRCode(fieldName: String)
}