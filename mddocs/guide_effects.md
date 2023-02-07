## Video Editor effects integration guide

- [Color filters](#Color-filters)
- [Stickers](#Stickers)
- [Transitions](#Transitions)

### Color filters
Any color filter(or LUT) is a pair of files texture and preview. The texture file should be stored in [assets/bnb-resources/luts](../app/src/main/assets/bnb-resources/luts) folder. 
Preview file is normally stored in a specific Android drawable folder i.e. [drawable-xhdpi](../app/src/main/res/drawable-xhdpi).
For example, color filter ```canada``` includes [texture file](../app/src/main/assets/bnb-resources/luts/canada.png) amd [preview file](../app/src/main/res/drawable-xhdpi/canada.png).  

You can move all available color filters in this sample to your project.

:exclamation: Important  
```AR Cloud``` does not support storing color filters.


By default, color filters are listed in alphabetical order.  

Create class [CustomColorFilterOrderProvider](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L258) and implement ```OrderProvider``` to provide custom order of color filters.
```kotlin
class CustomColorFilterOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("egypt", "byers")
}
```
:exclamation: Important  
These are names of specific color filters located in ```assets/bnb-resources/luts```.  

Next, use ```CustomColorFilterOrderProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L153)
```kotlin
override val colorFilterOrderProvider: BeanDefinition<OrderProvider> =
    single(named("colorFilterOrderProvider"), override = true) {
        CustomColorFilterOrderProvider()
    }
```

### Stickers(GIPHY)
Video Editor SDK has built in integration with [Giphy service](https://developers.giphy.com/docs/api/) for loading stickers.  
Any sticker effect represents a GIF file.  
 
To use stickers in your project you need to request personal [Giphy API key](https://support.giphy.com/hc/en-us/articles/360020283431-Request-A-GIPHY-API-Key).  
Set GIPHY API key to ```stickersApiKey``` parameter in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L172).

``` diff
single(override = true) {
    EditorConfig(
        ...
+       stickersApiKey = "<-- Paste Giphy key here to load gif images -->"
    )
}
```
Stickers will appear in the container on editor screen once ```stickersApiKey``` is set.

### FX 
FX(or visual) effect can be applied on a recorded video on editor screen i.e. ```Zoom```, ```Glitch```, etc. 

Video Editor has built in integration of managing these effects. The list of effects depends on license.

Implement  ```EnabledEffectsProvider``` interface in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt) to disable effects.
Here is an example of enabling just 2 effects ```Zoom```, ```Glitch```
```kotlin
single(named("fxEffectsProvider"), override = true) {
    EnabledEffectsProvider {
        listOf("zoom", "glitch")
    }
}
```

You can use ```DefaultEnabledFxEffectsProvider().provide()``` to get all effect names.

### Transitions
Transitions are effects applying to segue between two videos. Transition effects are not being played if the closest video (either to the left or to the right of transition icon) is very short. Transitions are enabled by default.


Set ```false``` to  ```supportsTransitions``` of ```EditorConfig``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L172) to disable transitions.
``` diff
single(override = true) {
    EditorConfig(
        ...
+       supportsTransitions = false
    )
}
```

Any applied transition effect is presented as a specific icon video edges.
![img](screenshots/transitions_1.png)
Use ```TrimmerTimelineViewStyle``` style to customize transition icon styles:

- **timelineTransitionIconBg** - background drawable for the icon
- **timelineTransitionIconViewSize** - the whole icon size
- **timelineTransitionIconImageSize** - the size of the picture inside transition icon

There is specific screen for selecting transition effects. You can customize it as well.
![img](screenshots/transitions_2.png)
- **trimmerTransitionsRecyclerView** - the container of transitions
- **trimmerTransitionItemParentStyle** - the container of the single transition
- **trimmerTransitionItemThumbStyle** - the selectable thumb
- **trimmerTransitionsItemImageStyle** - the picture inside the thumb
- **trimmerTransitionsItemTitleStyle** - the transition title


Video Editor includes 10 transition effects. Additional "empty" effect is used to leave the segue between videos without any transition effect.
Every transition effect has its own string identifier i.e. ```fade```:

| Transition effect | String identifier
| --- | --- |
| Empty | "empty" |
| Fade | "fade" |
| Spin | "spin" |
| Whip Down | "whip_down"|
| Whip Up | "whip_up" |
| Whip Right | "whip_right" |
| Whip Left | "whip_left" |
| Scroll Down | "scroll_down" |
| Scroll Up | "scroll_up" |
| Scroll Right | "scroll_right" |
| Scroll Left | "scroll_left" |

The identifier is used in naming of the following android resources related to every transition effect:
- picture on the selectable thumb
- title under the selectable thumb
- picture on the transition icon on the trimmer timeline

All these resources are named within SDK according to the specific scheme:

| Customization | Resource type | Resource name template | Example
| --- | --- | --- | --- |
| thumb icon | drawable | ic_transition_thumb_{id} | ic_transition_thumb_fade
| thumb title | string | transition_effect_{id} | transition_effect_empty
| timeline icon | drawable | ic_transition_{id} | ic_transition_whip_down

In this scheme {id} is a placeholder for the effect string identifier.

To customize any transition effect put your own value into android resource named according to the scheme above and place it into [res](../app/src/main/res) folder. This resource will override the default one.

For example, to change the title of ```Empty``` transition effect to ```Clear```
```xml
<string name="transition_effect_empty">Clear</string>
```
