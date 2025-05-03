package co.edu.udea.compumovil.gr07_20251.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import co.edu.udea.compumovil.gr07_20251.lab1.ui.theme.Labs20251Gr07Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Labs20251Gr07Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PersonalDataScreen()
                }
            }
        }
    }
}
