package app.view

import io.kvision.core.*
import io.kvision.navbar.*


fun Container.Header() = navbar(
  label = "KAMP",
  link = "/",
  type = NavbarType.STICKYTOP,
  nColor = NavbarColor.DARK,
  bgColor = BsBgColor.DARK,
) {
}
