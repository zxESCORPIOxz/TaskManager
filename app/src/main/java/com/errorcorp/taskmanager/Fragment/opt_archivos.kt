package com.errorcorp.taskmanager.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Adapter.AdapterArchivo
import com.errorcorp.taskmanager.Model.Archivo
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.MyApplication
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class opt_archivos : Fragment(), View.OnClickListener {

    private var selectedFileUri: Uri? = null

    //Imageview
    private lateinit var ivsubmit: ImageView
    private lateinit var ivicon: ImageView

    //Button
    private lateinit var btnsubir: FloatingActionButton
    private lateinit var btnagregar: MaterialButton
    private lateinit var btncancel: MaterialButton

    //EditText
    private lateinit var etname: EditText

    //TextView
    private lateinit var tvtitle: TextView
    private lateinit var tvsize: TextView

    //RelativeLayout
    private lateinit var ctagregar: RelativeLayout
    private lateinit var ctfile: RelativeLayout

    //ArrayList
    private var list_Archivo: ArrayList<Archivo> = ArrayList()

    //AdapterArchivo
    private lateinit var adapterArchivo: AdapterArchivo

    //RecyclerView
    private lateinit var rvList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_archivos, container, false)

        ctagregar = view.findViewById(R.id.ctagregar)
        ctfile = view.findViewById(R.id.ctfile)

        etname = view.findViewById(R.id.etname)

        tvtitle = view.findViewById(R.id.tvtitle)
        tvsize = view.findViewById(R.id.tvsize)

        rvList = view.findViewById(R.id.rvList)

        ivicon = view.findViewById(R.id.ivicon)
        ivsubmit = view.findViewById(R.id.ivsubmit)
        ivsubmit.setOnClickListener(this)

        btnsubir = view.findViewById(R.id.btnsubir)
        btnsubir.setOnClickListener(this)
        btnagregar = view.findViewById(R.id.btnagregar)
        btnagregar.setOnClickListener(this)
        btncancel = view.findViewById(R.id.btncancel)
        btncancel.setOnClickListener(this)

        listFilesInFirebaseStorage()

        return view
    }

    @SuppressLint("Range")
    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                selectedFileUri = uri

                val contentResolver = requireContext().contentResolver
                val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

                cursor?.use {
                    if (it.moveToFirst()) {
                        ctfile.visibility = View.VISIBLE

                        val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                        val size = it.getLong(sizeIndex)

                        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(displayName)
                        tvtitle.setText(displayName)
                        tvsize.setText(formatFileSize(size))
                        setIcon(fileExtension)
                    }
                }
            }
        } else {
            ctfile.visibility = View.GONE
        }
    }
    fun setIcon(contentType: String?) {
        when (contentType) {
            "pdf" -> {
                ivicon.setImageResource(R.drawable.ic_pdf)
            }
            "doc" -> {
                ivicon.setImageResource(R.drawable.ic_word)
            }
            "docx" -> {
                ivicon.setImageResource(R.drawable.ic_word)
            }
            "xls" -> {
                ivicon.setImageResource(R.drawable.ic_excel)
            }
            "xlsx" -> {
                ivicon.setImageResource(R.drawable.ic_excel)
            }
            "ppt" -> {
                ivicon.setImageResource(R.drawable.ic_power_point)
            }
            "pptx" -> {
                ivicon.setImageResource(R.drawable.ic_power_point)
            }
            "zip" -> {
                ivicon.setImageResource(R.drawable.ic_zip)
            }
            "rar" -> {
                ivicon.setImageResource(R.drawable.ic_rar)
            }
            "svg" -> {
                ivicon.setImageResource(R.drawable.ic_imagenes)
            }
            "png" -> {
                ivicon.setImageResource(R.drawable.ic_imagenes)
            }
            "jpg" -> {
                ivicon.setImageResource(R.drawable.ic_imagenes)
            }
            "apk" -> {
                ivicon.setImageResource(R.drawable.ic_apk)
            }
            "txt" -> {
                ivicon.setImageResource(R.drawable.ic_txt)
            }
            else -> {
                ivicon.setImageResource(R.drawable.ic_file)
            }
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnsubir) -> {
                ctagregar.visibility = View.VISIBLE
            }
            (R.id.btnagregar) -> {
                if(etname.text.isNotEmpty()){
                    uploadFileToFirebase()
                } else {
                    etname.setError("Campo obligatorio")
                }
            }
            (R.id.btncancel) -> {
                ctagregar.visibility = View.GONE
            }
            (R.id.ivsubmit) -> {
                openFilePicker()
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        filePickerLauncher.launch(intent)
    }


    private fun uploadFileToFirebase() {
        if (selectedFileUri != null) {
            CustomDialog.inicialization(requireContext())
            CustomDialog.showLoad()
            CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
                override fun onAnimationEnd() {
                    CustomDialog.dismiss()
                    listFilesInFirebaseStorage()
                    ctagregar.visibility = View.GONE
                    ctfile.visibility = View.GONE
                    etname.setText("")
                }
            })
            CustomDialog.setAnimationEndListenerFailed(object : CustomDialog.AnimationEndListener {
                override fun onAnimationEnd() {
                    CustomDialog.dismiss()
                }
            })
            val fileName = "${etname.text}"
            val fileRef = FirebaseStorage.getInstance().reference.child(
                "files" + "/"
                        + SharedPreferencesManager.getStringValue(Valor.DNI) +"/"
                        + fileName
            )
            fileRef.putFile(selectedFileUri!!)
                .addOnSuccessListener {
                    val archivo = Archivo()
                    archivo.nombre = it.storage.name
                    archivo.url = it.storage.downloadUrl.toString()
                    archivo.patch = it.storage.path
                    val fileFormat = getFormatFromContentType(it.metadata!!.contentType)
                    archivo.extension = fileFormat
                    archivo.sizeBytes = it.metadata!!.sizeBytes.toInt()
                    archivo.sizeFormat = formatFileSize(it.metadata!!.sizeBytes)
                    archivo.date = Date(it.metadata!!.updatedTimeMillis).toString()

                    saveFile(archivo)

                    CustomDialog.onSuccess()
                }
                .addOnFailureListener {
                    CustomDialog.onFailed()
                }
        }
    }

    private fun saveFile(archivo: Archivo) {
        MyApplication.getDatabaseInstance().archivoDao().insertArchivo(archivo)
    }

    private fun listFilesInFirebaseStorage() {
        list_Archivo.clear()

        list_Archivo.addAll(MyApplication.getDatabaseInstance().archivoDao().getAllArchivos())

        adapterArchivo = AdapterArchivo(list_Archivo, requireContext())

        rvList.adapter = adapterArchivo
        adapterArchivo.notifyDataSetChanged()
    }

    private fun getFormatFromContentType(contentType: String?): String {
        when (contentType) {
            "application/pdf" -> {
                return "pdf"
            }
            "application/msword" -> {
                return "doc"
            }
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> {
                return "docx"
            }
            "application/vnd.ms-excel" -> {
                return "xls"
            }
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> {
                return "xlsx"
            }
            "application/vnd.ms-powerpoint" -> {
                return "ppt"
            }
            "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> {
                return "pptx"
            }
            "application/zip" -> {
                return "zip"
            }
            "application/x-rar-compressed" -> {
                return "rar"
            }
            "application/rar" -> {
                return "rar"
            }
            "image/svg+xml" -> {
                return "svg"
            }
            "image/png" -> {
                return "png"
            }
            "image/jpeg" -> {
                return "jpg"
            }
            "application/vnd.android.package-archive" -> {
                return "apk"
            }
            "text/plain" -> {
                return "txt"
            }
            else -> return ""
        }
    }

    private fun formatFileSize(sizeBytes: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = sizeBytes.toDouble()
        var unitIndex = 0

        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }

        return "%.2f %s".format(size, units[unitIndex])
    }
}