package com.example.navegacao1.ui.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.navegacao1.model.dados.Usuario
import com.example.navegacao1.model.dados.UsuarioDAO

@Composable
fun TelaCadastro(modifier: Modifier = Modifier, onCadastroSuccess: () -> Unit) {
    val context = LocalContext.current
    val usuarioDAO = UsuarioDAO()

    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text(text = "Nome") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text(text = "Senha") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val usuario = Usuario(id = nome, nome = nome, senha = senha)
                scope.launch(Dispatchers.IO) {
                    usuarioDAO.adicionar(usuario) { novoUsuario ->
                        if (novoUsuario.id.isNotEmpty()) {
                            scope.launch(Dispatchers.Main) {
                                Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                onCadastroSuccess() // Navega de volta para a tela de login
                            }
                        } else {
                            scope.launch(Dispatchers.Main) {
                                mensagemErro = "Falha ao cadastrar usuário!"
                            }
                        }
                    }
                }
            }
        ) {
            Text("Cadastrar")
        }

        mensagemErro?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                mensagemErro = null
            }
        }
    }
}
