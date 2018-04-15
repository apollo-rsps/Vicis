package rs.emulate.editor.resource.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rs.emulate.editor.resource.ModelResource
import rs.emulate.editor.resource.ResourceIdentifier
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.model.ModelDecoder
import rs.emulate.shared.util.DataBuffer
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.zip.GZIPInputStream


@Component
class ModelResourceProvider : ResourceProvider<ModelResource> {
    override val lazyLoads = true

    @Autowired lateinit var cache: IndexedFileSystem

    companion object {
        val RESOURCE_NAME = "Models"
        val RESOURCE_INDEX = 1
    }

    override fun resourceName() = RESOURCE_NAME

    override fun provide(identifier: ResourceIdentifier): ResourceProviderResult<ModelResource> {
        return when (identifier) {
            is ResourceIdentifier.FileDescriptor -> {
                try {
                    val compressedData = cache.getFile(identifier.index, identifier.file).array()
                    val decompressor = GZIPInputStream(ByteArrayInputStream(compressedData))
                    val decompressedData = DataBuffer.wrap(decompressor.readBytes())
                    val decoder = ModelDecoder(decompressedData)
                    val model = decoder.decode()
                    ResourceProviderResult.Found(ModelResource("Model: ${identifier.index}", identifier, model))
                } catch (exception: IOException) {
                    ResourceProviderResult.NotFound<ModelResource>()
                }
            }
            else -> ResourceProviderResult.NotSupported()
        }
    }

    override fun listAll(): List<ResourceIdentifier> {
        return (0..cache.getFileCount(RESOURCE_INDEX)).map {
            ResourceIdentifier.FileDescriptor(
                RESOURCE_INDEX,
                it
            )
        }
    }

}
