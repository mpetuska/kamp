package app.component.navbar

import app.util.*
import io.github.mpetuska.khakra.icons.*
import io.github.mpetuska.khakra.kt.*
import io.github.mpetuska.khakra.layout.*
import kotlinext.js.*
import react.*


private val Logo by FC {
  Box({
    w = "100px"
    color = arrayOf("white", "white", "primary.500", "primary.500")
  }) {
    Text({
      fontSize = "lg"
      fontWeight = "bold"
    }) {
      +"Logo"
//      Image({
//        width = "30px"
//        src = "/kamp.svg"
//        alt = "Logo Component"
//      })
    }
  }
}

private external interface MenuToggleProps : RProps {
  var toggle: () -> Unit
  var isOpen: Boolean
}

private val MenuToggle by FC<MenuToggleProps> { props ->
  Box({
    this["onClick"] = props.toggle
    display = js {
      base = "block"
      md = "none"
    }
  }) {
    if (props.isOpen) {
      CloseIcon()
    } else {
      HamburgerIcon()
    }
  }
}

private external interface MenuItemProps : RProps

private val MenuItem by FC<MenuItemProps> {

}

private val Header by FC {
  var open by useState(false)
  fun toggle() {
    open = !open
  }
  
  MenuToggle {
    attrs {
      toggle = ::toggle
      isOpen = open
    }
  }
}

val NavbarContainer by FC {
  Flex({
    `as` = "nav"
    align = "center"
    justify = "space-between"
    wrap = "wrap"
    w = "100%"
    mb = 8
    p = 8
    bg = arrayOf("primary.500", "primary.500", "transparent", "transparent")
    color = arrayOf("gray", "gray", "primary.700", "primary.700")
  }) {
    it.children()
  }
}

val Navbar by FC {
  var open by useState(false)
  fun toggle() {
    open = !open
  }
  NavbarContainer {
    Logo {}
    MenuToggle {
      attrs {
        toggle = ::toggle
        isOpen = open
      }
    }
  }
}
