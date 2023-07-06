package dev.petuska.kodex.client.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.I

/**
 * Regular FontAwesome icons (`fa-regular`)
 *
 * [Icon Search](https://fontawesome.com/search?o=r&s=regular&f=classic&m=free)
 * @param icon name of the icon like `fa-house`
 */
@Composable
fun FarIcon(icon: String) = I({ classes("fa-regular", icon) })

/**
 * Solid FontAwesome icons (`fa-solid`)
 *
 * [Icon Search](https://fontawesome.com/search?o=r&s=solid&f=classic&m=free)
 * @param icon name of the icon like `fa-house`
 */
@Composable
fun FasIcon(icon: String) = I({ classes("fa-solid", icon) })

/**
 * Brand FontAwesome icons (`fa-brands`)
 *
 * [Icon Search](https://fontawesome.com/search?o=r&f=brands&m=free)
 * @param icon name of the icon like `fa-github`
 */
@Composable
fun FabIcon(icon: String) = I({ classes("fa-brands", icon) })
