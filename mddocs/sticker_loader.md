# Custom StickerLoader

If you want use custom loader for sticker, you should implement `StickerLoader` interface.

```kotlin
interface StickerLoader {

    @Throws(StickerLoadingException::class)
    suspend fun loadStickers(query: String, offset: Int): StickerBundle

    suspend fun loadStickerFile(destinationFile: File, url: String)
}
```

Function `loadStickers` is used to get a list of stickers, where the parameters are:

- `query` needed to search for stickers upon request
- `offset` needed to set offset if you want to use pagination

Function `loadStickers` returns `StickerBundle`

```kotlin
data class StickerBundle(
    val stickers: List<Sticker>,
    val pagination: StickerPaginationResult
)

data class Sticker(
    val id: String,
    val previewUrl: String,
    val originalUrl: String,
    val title: String
)

data class StickerPaginationResult(
    val offset: Int,
    val totalCount: Int,
    val count: Int
)
```

Function `loadStickerFile` is used to load sticker, where the parameters are:

- `destinationFile` used to set the file to which the sticker will be saved
- `url` the link for the sticker you want to download

Example of custom StickerLoad implementation:

```kotlin
CustomStickerLoader(
    private val api: CustomStickerApi
) : StickerLoader {

    override suspend fun loadStickers(query: String, offset: Int): StickerBundle {
        val result: StickerResponse = if (query.isEmpty()) {
            api.getStickers(offset)
        } else {
            api.searchSticker(query, offset)
        }
        return result.mapToStickerBundle()
    }

    override suspend fun loadStickerFile(destinationFile: File, url: String) {
        val result = api.downloadStickerFile(url)
        runCatching {
            result.bytes().inputStream().copyTo(destinationFile.outputStream())
        }
    }

    private fun StickerResponse.mapToStickerBundle() {
			//map sticker response to StickerBundle
		}

}
```

Where `CustomStickerApi` is your class for working with an api that provides an interface for working with stickers.

Also you should override you `CustomStickerLoader` in your own `VideoEditorKoinModule`.

```kotlin
class VideoEditorKoinModule : FlowEditorModule() {
		
    ...

    override val stickerLoader: BeanDefinition<StickerLoader> = single {
        CustomStickerLoader(
            apiGiphy = CustomStickerApi()
        )
    }
}
```