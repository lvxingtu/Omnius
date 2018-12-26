package me.chill.styles

import javafx.scene.Cursor.HAND
import javafx.scene.paint.Color.TRANSPARENT
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.c
import tornadofx.px

// TODO: Move the colors into a separate class
class EditingAreaStyles : Stylesheet() {
  companion object {
    private val blueAccent = c("#448AFF")
  }

  init {
    splitPaneDivider {
      backgroundColor += TRANSPARENT
      padding = box(1.px)
    }

    select(".tab-pane:focused > .tab-header-area > .headers-region > .tab:selected .focus-indicator") {
      borderColor += box(TRANSPARENT)
    }

    // TODO: Fix the weird resizing of the box whenever the item is leaving focus
    tab {
      backgroundColor += TRANSPARENT
      padding = box(5.px, 10.px)

      and(hover) {
        cursor = HAND
      }

      and(selected) {
        borderColor += box(TRANSPARENT, TRANSPARENT, blueAccent, TRANSPARENT)
        borderWidth += box(0.px, 0.px, 5.px, 0.px)
      }
    }

    select(".tab-close-button") {
      and(hover) {
        backgroundColor += blueAccent
      }
    }

    select(".tab-header-area") {
      padding = box(0.px)
    }

    select(".tab-label") {
      labelPadding = box(0.px, 10.px, 0.px, 10.px)
    }
  }
}