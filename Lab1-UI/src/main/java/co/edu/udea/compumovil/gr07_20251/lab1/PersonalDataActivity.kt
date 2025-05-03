package co.edu.udea.compumovil.gr07_20251.lab1

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import android.widget.DatePicker
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.viewinterop.AndroidView
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            androidx.compose.material3.MaterialTheme {
                PersonalDataScreen()
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (day: Int, month: Int, year: Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    AndroidView(
        factory = { context ->
            DatePicker(context).apply {
                init(year, month, day) { _, selectedYear, selectedMonth, selectedDay ->
                    onDateSelected(selectedDay, selectedMonth, selectedYear)
                }
            }
        },
        modifier = Modifier.wrapContentSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen() {
    val context = LocalContext.current
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var educationLevel by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val educationLevels = listOf(
        "Primaria", "Secundaria", "Técnico", "Universitario", "Posgrado"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.personal_data_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = {
                Text(
                    text = stringResource(R.string.first_name) + " *",
                    color = if (firstName.isBlank()) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Nombre",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = firstName.isBlank()
        )


        // Apellido
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text(
                    text = stringResource(R.string.last_name) + " *",
                    color = if (lastName.isBlank()) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Apellido",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = lastName.isBlank()
        )

        // Gender Radio Buttons
        Text(text = stringResource(R.string.gender))
        Row {
            RadioButton(
                selected = gender == "Male",
                onClick = { gender = "Male" }
            )
            Text(
                text = stringResource(R.string.male),
                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = gender == "Female",
                onClick = { gender = "Female" }
            )
            Text(
                text = stringResource(R.string.female),
                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
            )
        }

        // Date of Birth
        Text(text = stringResource(R.string.birth_date))
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                onDateSelected = { day, month, year ->
                    birthDate = "$day/${month + 1}/$year"
                    showDatePicker = false
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                label = {
                    Text(
                        text = stringResource(R.string.birth_date) + " *",
                        color = if (birthDate.isBlank()) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                isError = birthDate.isBlank(),
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
                }
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { showDatePicker = true }
            )
        }

        // Education Level
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = educationLevel,
                onValueChange = {},
                label = { Text(stringResource(R.string.education_level)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                educationLevels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(text = level) },
                        onClick = {
                            educationLevel = level
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                when {
                    firstName.isBlank() -> {
                        Toast.makeText(context, "Por favor ingrese sus nombres", Toast.LENGTH_SHORT).show()
                    }
                    firstName.length < 2 -> {
                        Toast.makeText(context, "El nombre es demasiado corto", Toast.LENGTH_SHORT).show()
                    }
                    lastName.isBlank() -> {
                        Toast.makeText(context, "Por favor ingrese sus apellidos", Toast.LENGTH_SHORT).show()
                    }
                    lastName.length < 2 -> {
                        Toast.makeText(context, "El apellido es demasiado corto", Toast.LENGTH_SHORT).show()
                    }
                    birthDate.isBlank() -> {
                        Toast.makeText(context, "Por favor seleccione su fecha de nacimiento", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.d("PersonalData", "Información personal:")
                        Log.d("PersonalData", "Nombres: $firstName")
                        Log.d("PersonalData", "Apellidos: $lastName")
                        Log.d("PersonalData", "Género: ${gender.ifEmpty { "No especificado" }}")
                        Log.d("PersonalData", "Fecha nacimiento: $birthDate")
                        Log.d("PersonalData", "Escolaridad: ${educationLevel.ifEmpty { "No especificada" }}")

                        context.startActivity(Intent(context, ContactDataActivity::class.java))
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}
