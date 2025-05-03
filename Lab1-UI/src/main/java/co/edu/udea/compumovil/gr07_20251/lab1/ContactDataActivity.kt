package co.edu.udea.compumovil.gr07_20251.lab1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class ContactDataActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { ContactDataScreen() }
        }
    }
}

@Composable
fun ContactDataScreen() {
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    val context = LocalContext.current

    val countries = listOf(
        "Argentina", "Bolivia", "Brasil", "Chile", "Colombia",
        "Costa Rica", "Cuba", "Ecuador", "El Salvador", "Guatemala",
        "Honduras", "México", "Nicaragua", "Panamá", "Paraguay",
        "Perú", "República Dominicana", "Uruguay", "Venezuela"
    )

    val colombianCities = listOf(
        "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena",
        "Bucaramanga", "Pereira", "Manizales", "Cúcuta", "Ibagué"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.contact_data_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = {
                Text(
                    text = stringResource(R.string.phone) + " *",
                    color = if (phone.isBlank()) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = phone.isBlank()
        )

        // Address
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(R.string.address)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Next),
            singleLine = true
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = it.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = {
                Text(
                    text = stringResource(R.string.email) + " *",
                    color = if (email.isBlank() || !isEmailValid) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = !isEmailValid,
            trailingIcon = {
                if (email.isNotEmpty()) {
                    Icon(
                        imageVector = if (isEmailValid) Icons.Default.Check else Icons.Default.Clear,
                        contentDescription = if (isEmailValid) "Email válido" else "Email inválido",
                        tint = if (isEmailValid) Color.Green else Color.Red
                    )
                }
            }
        )

        // Country
        Text(
            text = stringResource(R.string.country) + " *",
            color = if (country.isBlank()) Color.Red else MaterialTheme.colorScheme.onSurface
        )
        CountryAutoComplete(
            countries = countries,
            value = country,
            onValueChange = { country = it },
            isError = country.isBlank()
        )

        // Ciudad con autocomplete
        Text(text = stringResource(R.string.city))
        CountryAutoComplete(
            countries = colombianCities,
            value = city,
            onValueChange = { city = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                when {
                    phone.isBlank() -> {
                        Toast.makeText(context, "Por favor ingrese su teléfono", Toast.LENGTH_SHORT).show()
                    }
                    email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        Toast.makeText(context, "Por favor ingrese un email válido", Toast.LENGTH_SHORT).show()
                    }
                    country.isBlank() -> {
                        Toast.makeText(context, "Por favor seleccione su país", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.d("ContactData", "Información de contacto:")
                        Log.d("ContactData", "Teléfono: $phone")
                        Log.d("ContactData", "Dirección: $address")
                        Log.d("ContactData", "Email: $email")
                        Log.d("ContactData", "País: $country")
                        Log.d("ContactData", "Ciudad: $city")

                        // Cerrar la actividad o mostrar mensaje de éxito
                        Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.submit))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryAutoComplete(
    countries: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(value) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Corregido: usar onExpandedChange
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it)
            },
            label = { Text(stringResource(R.string.country)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            isError = isError,
            readOnly = false
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // onDismissRequest va aquí
        ) {
            countries.filter { it.contains(textFieldValue, ignoreCase = true) }
                .forEach { countryOption ->
                    DropdownMenuItem(
                        text = { Text(text = countryOption) },
                        onClick = {
                            textFieldValue = countryOption
                            onValueChange(countryOption)
                            expanded = false
                        }
                    )
                }
        }
    }
}