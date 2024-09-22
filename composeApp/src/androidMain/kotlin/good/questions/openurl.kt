package good.questions

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext


@Composable
actual fun openUrl(url: String ,open:Int ) {
    val context = LocalContext.current
    LaunchedEffect(open) {
     if(open>0) {
         val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
         context.startActivity(intent)
     }
    }
}