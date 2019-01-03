package me.chill.models

import com.google.gson.JsonObject
import me.chill.actionmap.ActionMap
import me.chill.actionmap.ActionMap.*
import me.chill.actionmap.ActionMapObservable
import me.chill.actionmap.ActionMapObserver
import me.chill.configuration.ConfigurationManager
import me.chill.configuration.ConfigurationManager.configuration
import me.chill.views.Editor
import me.chill.views.ToolBar.Position.LEFT
import me.chill.views.ToolBar.Position.TOP
import java.io.File

/**
 * Model for the editor - stores user preferences that can be loaded from the settings file
 * using [ConfigurationManager].
 *
 * Editing the properties of the model will notify the [Editor] and the view will react accordingly
 * to the changes made.
 *
 * Is both an [ActionMapObservable] and [ActionMapObserver]:
 * - The [Editor] view observes this model for changes in the properties.
 * - Observes [ConfigurationManager] for changes in the configurations.
 */
// TODO: Change the configuration property changes to another observer interface to prevent confusion
object EditorModel : ActionMapObservable, ActionMapObserver {

  private val listeners = mutableListOf<ActionMapObserver>()

  init {
    ConfigurationManager.addObserver(this)
  }

  var toolBarPosition = configuration.toolBarPosition
    set(value) {
      field = value
      notifyObservers(
        when (value) {
          TOP -> MOVE_TOOLBAR_TOP
          LEFT -> MOVE_TOOLBAR_LEFT
        }
      )
    }

  var toolBarVisibility = configuration.toolBarVisibility
    private set(value) {
      field = value
      notifyObservers(TOGGLE_TOOLBAR_VISIBILITY)
    }

  var currentFolder: File? = with(configuration.previousOpenFolderPath) {
    this ?: return@with null
    File(this)
  }
    set(value) {
      field = value
      notifyObservers(FOLDER_CHANGED)
    }

  var fontSize = configuration.fontSize
    set(value) {
      field = value
      notifyObservers(FONT_SIZE_CHANGED)
    }

  var fontFamily = configuration.fontFamily
    set(value) {
      field = value.toList()
      notifyObservers(FONT_FAMILY_CHANGED)
    }

  override fun addObserver(actionMapObserver: ActionMapObserver) {
    listeners.add(actionMapObserver)
  }

  override fun removeObserver(actionMapObserver: ActionMapObserver) {
    listeners.remove(actionMapObserver)
  }

  override fun notifyObservers(actionMap: ActionMap, data: JsonObject?) {
    listeners.forEach { it.update(actionMap, data) }
  }

  override fun update(actionMap: ActionMap, data: JsonObject?) {
    when (actionMap) {
      OPTIONS_SAVE -> updateState()
      else -> return
    }
  }

  fun toggleToolBarVisibility() {
    toolBarVisibility = !toolBarVisibility
    notifyObservers(TOGGLE_TOOLBAR_VISIBILITY, null)
  }

  private fun updateState() {
    toolBarPosition = configuration.toolBarPosition
    toolBarVisibility = configuration.toolBarVisibility
    fontSize = configuration.fontSize
    fontFamily = configuration.fontFamily
  }
}