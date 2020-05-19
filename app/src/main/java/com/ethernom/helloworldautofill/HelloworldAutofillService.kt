package com.ethernom.helloworldautofill

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.assist.AssistStructure
import android.content.Intent
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.*
import android.util.Log
import android.view.autofill.AutofillValue.forText
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi


var dataFields:MutableMap<String,AssistStructure.ViewNode> = mutableMapOf()
var presentation: RemoteViews? = null
@SuppressLint("Registered")
@RequiresApi(Build.VERSION_CODES.O)
class HelloworldAutofillService : AutofillService () {
    val TAG:String = "HelloworldAutofill"
    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {

        dataFields = mutableMapOf()

        MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : onFillRequest c \n")


        Log.d(TAG,"onFillRequest")
        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure

        var nodes = structure.windowNodeCount
        Log.d(TAG, "Nodes ${nodes}")
        for (i in 0 until nodes) {
            val node = structure.getWindowNodeAt(i).rootViewNode
            calulateNode(
                node
            )
        }

        var responseBuilder =  FillResponse.Builder();

        presentation = newRemoteViews(packageName, "Ethernom Helloworld", R.drawable.ethernomlogo)

        var intent =  Intent(this, AuthActivity::class.java)
        var sender = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT).intentSender;

        var datasetBuilder =  Dataset.Builder(presentation!!);

        //Main difference here:
        datasetBuilder.setAuthentication(sender);

        Log.d(TAG,"emailFields.size: ${dataFields.size}")

        if( dataFields.isEmpty()) {
            return
        }
        if(dataFields.size == 1) {
            if(dataFields["Username"] != null) {
                datasetBuilder.setValue(dataFields["Username"]!!.autofillId!!, forText("some placeholder data"))
            } else {
                datasetBuilder.setValue(dataFields["Password"]!!.autofillId!!, forText("some placeholder data"))
            }

        } else {
            datasetBuilder.setValue(dataFields["Username"]!!.autofillId!!, forText("some placeholder data"))
            datasetBuilder.setValue(dataFields["Password"]!!.autofillId!!, forText("some placeholder data"))

        }

        responseBuilder.addDataset(datasetBuilder.build())

        callback.onSuccess(responseBuilder.build())

    }


    override fun onSaveRequest(p0: SaveRequest, p1: SaveCallback) {
        Log.d(TAG,"onSaveRequest")

    }

    fun newRemoteViews(packageName: String, remoteViewsText: String,
                       @DrawableRes drawableId: Int): RemoteViews {
        val presentation = RemoteViews(packageName, R.layout.multidataset_service_list_item)
        presentation.setTextViewText(R.id.text, remoteViewsText)
        presentation.setImageViewResource(R.id.icon, drawableId)
        return presentation
    }


    fun calulateNode(
        node: AssistStructure.ViewNode
    ) {
        Log.d(TAG, "node.className: ${node.className}")
        Log.d(TAG, "node.className: ${node.childCount}")

        if(node.className == null) {
            Log.d(TAG, "hint: ${node.hint}")
            if (node.hint != null && (node.hint.toUpperCase().contains("EMAIL") || node.hint.toUpperCase().contains("USERNAME") || node.hint.toUpperCase().contains("IDENTIFIER") )) {
                Log.d(TAG, "node: $node")
                MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : Email field \n")

                if(dataFields["Username"] == null) {
                    dataFields.put("Username", node)
                }
            } else if(node.hint != null && node.hint.toUpperCase().contains("PASSWORD")) {
                Log.d(TAG, "node: $node")
                MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : Password field \n")
                if(dataFields["Password"] == null) {

                    dataFields.put("Password", node)

                }

            }
            return
        }
        // More code goes here
        if(node.className == null) {
            return
        }
        if(node.className.contains("WebView")) {
            Log.d(TAG, "WebView"+ node.webDomain)
        }

            if (node.className.contains("EditText")) {
                Log.d(TAG, "identifyEmailFields")
                var viewId = node.hint
                Log.d(TAG, "viewId $viewId")
                Log.d(TAG, "viewId ${node.idEntry}")


                var viewAutoFill = node.autofillHints

                if(viewId == null) {
                    if(viewAutoFill != null ) {
                        Log.d(TAG, "viewAutoFill ${viewAutoFill[0]}")
                        viewId = viewAutoFill[0]
                    }
                    else if (node.idEntry != null) {
                        viewId = node.idEntry
                    }else if (node.hint != null) {
                        Log.d(TAG, "hint ${node.hint}")
                    }
                }


                if(viewId != null ) {
                    if (viewId.toUpperCase().contains("EMAIL") || viewId.toUpperCase().contains("USERNAME") || viewId.toUpperCase().contains("IDENTIFIER")) {
                        Log.d(TAG, "node: $node")
                        MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : Email field \n")

                        if(dataFields["Username"] == null){
                            dataFields.put("Username",node)
                        }
                        return
                    } else if(viewId.toUpperCase().contains("PASSWORD")) {
                        Log.d(TAG, "node: $node")
                        MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : Password field \n")
                        if(dataFields["Password"] == null) {
                            dataFields.put("Password", node)
                        }
                        return

                    }
                }

            }


        for (i in 0 until node.childCount) {
            calulateNode(node.getChildAt(i))
        }
    }
}
