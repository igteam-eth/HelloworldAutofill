package com.ethernom.helloworldautofill

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.view.autofill.AutofillManager
import android.view.autofill.AutofillValue.forText
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity(), ListAdapter.AdapterCallback {
    val mUserData = listOf(
//        UserData("Sophaknbc@gmail.com", "12345",),
//        UserData("phamemot@gmail.com", "Loy8loy9"),
//        UserData("igteam.ethernom@gmail.com", "amazon@#\$"),
//        UserData("money@yahoo.com", "78967")

        UserData("Tiktok","tiktok_user_test","mnbvcxz0987654321"),
        UserData("Instagram","igteam.ethernom"	         ,"lkj098"),
        UserData("Gmail","igteam.ethernom@gmail.com","@@@###$$$"),
        UserData("Facebook","igteam.ethernom@gmail.com","facebookPassword"),
        UserData("Snapchat","igteamethernom",	            "snapchat@#$"),
        UserData("Netflix","igteam.ethernom@gmail.com","netflix@#$"),
        UserData("Amazon","igteam.ethernom@gmail.com","amazon@#$"),
        UserData("Walmart","igteam.ethernom@gmail.com",	"walmart@#$"),
        UserData("Twitter","igteam.ethernom@gmail.com",	"@@@###$$$"),
        UserData("PayPal","igteam.ethernom@gmail.com","IG123123"),
        UserData("Reddit","ig_user_test",	       "IG123123"),
        UserData("Microsof Outlook","igteam.ethernom@gmail.com","IG123123"),
        UserData("Kickstarter","igteam.ethernom@gmail.com","IG123123"),
        UserData("Yahoo Mail","igteam.ethernom@yahoo.com","@@@###$$$"),
        UserData("Dropbox","igteam.ethernom@gmail.com","IG123123")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)



//        list_recycler_view.apply {
//            // set a LinearLayoutManager to handle Android
//            // RecyclerView behavior
//            layoutManager = LinearLayoutManager(this@AuthActivity)
//            // set the custom adapter to the RecyclerView
//            adapter = ListAdapter(mUserData, this@AuthActivity)
//        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun itemClickCallBack(position: Int) {

//        var presentation = newRemoteViews(packageName, "dataset from auth activity", R.drawable.ethernomlogo)

        var datasetBuilder =  Dataset.Builder(presentation!!)

        MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : Username:  ${mUserData[position].username} \n")
        MyHelloworldApplication.appendLog("${MyHelloworldApplication.getCurrentDate()} : Password:  ${mUserData[position].password} \n")

        if(dataFields.size == 1) {
            if(dataFields["Username"] != null) {
                datasetBuilder.setValue(dataFields["Username"]!!.autofillId!!, forText(mUserData[position].username))
            } else {
                datasetBuilder.setValue(dataFields["Password"]!!.autofillId!!, forText(mUserData[position].password))
            }

        } else {
            datasetBuilder.setValue(dataFields["Username"]!!.autofillId!!, forText(mUserData[position].username))
            datasetBuilder.setValue(dataFields["Password"]!!.autofillId!!, forText(mUserData[position].password))
        }

        var ReplyIntent = Intent()

        var returnDataset = true; //tried both true and false, neither works
        if (returnDataset)
        {
            ReplyIntent.putExtra(AutofillManager.EXTRA_AUTHENTICATION_RESULT, datasetBuilder.build())
        }
        else
        {
            var responseBuilder =  FillResponse.Builder()
            responseBuilder.addDataset(datasetBuilder.build())
            ReplyIntent.putExtra(AutofillManager.EXTRA_AUTHENTICATION_RESULT, responseBuilder.build())
        }
        setResult(Activity.RESULT_OK, ReplyIntent)
        dataFields= mutableMapOf()

        finish()
    }

    fun newRemoteViews(packageName: String, remoteViewsText: String,
                       @DrawableRes drawableId: Int): RemoteViews {
        val presentation = RemoteViews(packageName, R.layout.multidataset_service_list_item)
        presentation.setTextViewText(R.id.text, remoteViewsText)
        presentation.setImageViewResource(R.id.icon, drawableId)
        return presentation
    }

}
