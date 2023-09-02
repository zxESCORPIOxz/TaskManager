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
import android.widget.SearchView
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
import com.errorcorp.taskmanager.Util.Util
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class opt_archivos : Fragment(), View.OnClickListener {

    private var selectedFileUri: Uri? = null

    //SearchView
    private lateinit var svbuscar: SearchView

    //Imageview
    private lateinit var ivsubmit: ImageView
    private lateinit var ivicon: ImageView

    private lateinit var ivall: ImageView
    private lateinit var ivaab: ImageView
    private lateinit var ivvideo: ImageView
    private lateinit var ivmusic: ImageView
    private lateinit var ivimage: ImageView
    private lateinit var ivrar: ImageView
    private lateinit var ivzip: ImageView
    private lateinit var ivword: ImageView
    private lateinit var ivexcel: ImageView
    private lateinit var ivppt: ImageView
    private lateinit var ivapk: ImageView
    private lateinit var ivpdf: ImageView
    private lateinit var ivtxt: ImageView
    private lateinit var ivfile: ImageView

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


        ivall = view.findViewById(R.id.ivall)
        ivall.setOnClickListener(this)
        ivaab = view.findViewById(R.id.ivaab)
        ivaab.setOnClickListener(this)
        ivvideo = view.findViewById(R.id.ivvideo)
        ivvideo.setOnClickListener(this)
        ivmusic = view.findViewById(R.id.ivmusic)
        ivmusic.setOnClickListener(this)
        ivimage = view.findViewById(R.id.ivimage)
        ivimage.setOnClickListener(this)
        ivrar = view.findViewById(R.id.ivrar)
        ivrar.setOnClickListener(this)
        ivzip = view.findViewById(R.id.ivzip)
        ivzip.setOnClickListener(this)
        ivword = view.findViewById(R.id.ivword)
        ivword.setOnClickListener(this)
        ivexcel = view.findViewById(R.id.ivexcel)
        ivexcel.setOnClickListener(this)
        ivppt = view.findViewById(R.id.ivppt)
        ivppt.setOnClickListener(this)
        ivapk = view.findViewById(R.id.ivapk)
        ivapk.setOnClickListener(this)
        ivpdf = view.findViewById(R.id.ivpdf)
        ivpdf.setOnClickListener(this)
        ivtxt = view.findViewById(R.id.ivtxt)
        ivtxt.setOnClickListener(this)
        ivfile = view.findViewById(R.id.ivfile)
        ivfile.setOnClickListener(this)

        btnsubir = view.findViewById(R.id.btnsubir)
        btnsubir.setOnClickListener(this)
        btnagregar = view.findViewById(R.id.btnagregar)
        btnagregar.setOnClickListener(this)
        btncancel = view.findViewById(R.id.btncancel)
        btncancel.setOnClickListener(this)

        svbuscar = view.findViewById(R.id.svbuscar)

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
                        ivicon.setImageResource(Util.getResByExtension(fileExtension))
                    }
                }
            }
        } else {
            ctfile.visibility = View.GONE
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
            (R.id.ivall) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_all)
                ivall.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivaab) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_aab)
                ivaab.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivvideo) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_video)
                ivvideo.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivmusic) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_music)
                ivmusic.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivimage) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_imagenes)
                ivimage.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivrar) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_rar)
                ivrar.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivzip) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_zip)
                ivzip.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivword) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_word)
                ivword.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivexcel) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_excel)
                ivexcel.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivppt) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_power_point)
                ivppt.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivapk) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_apk)
                ivapk.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivpdf) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_pdf)
                ivpdf.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivtxt) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_txt)
                ivtxt.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivfile) -> {
                cleanSelectedCategory()
                adapterArchivo.filtradoByCategoria(R.drawable.ic_file)
                ivfile.setBackgroundResource(R.drawable.ripple_butons_category)
            }
        }
    }

    fun cleanSelectedCategory(){
        ivall.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivaab.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivvideo.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivmusic.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivimage.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivrar.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivzip.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivword.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivexcel.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivppt.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivapk.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivpdf.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivtxt.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivfile.setBackgroundResource(R.drawable.ripple_butons_category_on)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        filePickerLauncher.launch(intent)
    }

    private fun uploadFileToFirebase() {
        if (selectedFileUri != null) {
            CustomDialog.inicialization(requireContext())
            CustomDialog.showLoad(R.raw.anim_upload_file)
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
                    val fileFormat = MimeTypeMap.getFileExtensionFromUrl(tvtitle.text.toString())
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

        svbuscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cleanSelectedCategory()
                ivall.setBackgroundResource(R.drawable.ripple_butons_category)
                adapterArchivo.filtradoByText(newText)
                return false
            }
        })
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