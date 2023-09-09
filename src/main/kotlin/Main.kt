import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ui.menu.MenuScreen
import ui.menu.MenuViewModel


fun main() = application {
    val context = runApplication<SpringJpaComposeApp>()
    val viewModel = context.getBean(MenuViewModel::class.java)

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            height = 700.dp
        )
    ) {
        val uiState by viewModel.uiState.collectAsState()
        MaterialTheme {
            MenuScreen(
                context = context,
                uiState = uiState,
                updateActiveAccount = { viewModel.updateActiveAccount(it) },
                openAccountsList = { viewModel.openAccountList(it) },
                closeAccountsList = { viewModel.closeAccountsList() }
            )
        }
    }
}

@SpringBootApplication(exclude = [R2dbcAutoConfiguration::class])
@EnableJpaRepositories(basePackages = ["data.repository"])
@ComponentScan(basePackages = ["ui.list", "ui.menu"])
@EntityScan(basePackages = ["data.database.entities"])
open class SpringJpaComposeApp


