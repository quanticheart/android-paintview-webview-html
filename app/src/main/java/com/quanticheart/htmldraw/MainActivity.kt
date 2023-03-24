package com.quanticheart.htmldraw

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.enableSlowWholeDocumentDraw()
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.interface_web)
        val fab = findViewById<FloatingActionButton>(R.id.print)

        // opening the html file in webview
        webView.loadUrl("file:///android_asset/index.html")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.webChromeClient = object : WebChromeClient() {}
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.setAppCacheMaxSize(8 * 1024 * 1024)
        webView.settings.setAppCachePath(applicationContext.cacheDir.absolutePath)
        webView.settings.setAppCacheEnabled(true)

        webView.addJavascriptInterface(this, "Android")

        fab.setOnClickListener {
//            webView.evaluateJavascript("enable('test send data to webview');", null)
            webView.evaluateJavascript("print();", null)
        }
    }

    @JavascriptInterface
    fun print(bitmap: Bitmap?) {
        bitmap?.let {
            share(bitmap)
        } ?: run {
            Toast.makeText(this, "Image empty", Toast.LENGTH_SHORT).show()
        }
    }

    @JavascriptInterface
    fun data(data: String) {
        Log.e("DATA from webview", data)
        val decodedString: ByteArray = Base64.decode(data.split(",")[1], Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        share(decodedByte)
    }

    @JavascriptInterface
    fun callWebViewFunction(): String? {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        //add support for jpg and more.
//        bitMap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
//        val byteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
        return "TEST"
    }

    @JavascriptInterface
    fun wWebViewCallFunction(): String? {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        //add support for jpg and more.
//        bitMap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
//        val byteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
        return "TEST"
    }

    private fun sharePalette(bitmap: Bitmap) {
        val bitmapPath =
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "palette", "share palette")
        val bitmapUri = Uri.parse(bitmapPath)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun share(bitmap: Bitmap?) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "image/*"
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap))
        try {
            startActivity(Intent.createChooser(i, "My Profile ..."))
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        // Define a bitmap with the same size as the view
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        // Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        // Get the view's background
        val bgDrawable: Drawable? = view.background
        if (bgDrawable != null) // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  // does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        // return the bitmap
        return returnedBitmap
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
}

