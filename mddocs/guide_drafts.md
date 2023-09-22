# Drafts guide

```DraftsHelper``` interface is used for managing drafts.

```kotlin
interface DraftsHelper {

    val allDrafts: StateFlow<List<Draft>>

    fun delete(draft: Draft)

    fun deleteAll()

    fun openDraft(draft: Draft): Intent

    fun openLastDraft(): Intent
}
```

To get the instance of ```DraftsHelper```  use the following in your Fragment or Activity.
```kotlin
val draftsHelper: DraftsHelper by inject()
```

### Used string resources

| ResourceId        |      Value      |
| ------------- | :----------- |
| drafts_title | Drafts |
| drafts_empty_description | No Drafts |
| drafts_options_edit | Edit |
| drafts_options_delete | Delete |
| editor_trim_video | Adjust Clips |
| editor_discard_changes | Discard changes |
| editor_update_draft | Update draft |
