package org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

object Zip {

    fun extractFolder(zipFile: File, extractFolder: File): File {
        try {
            val zip = ZipFile(zipFile)

            extractFolder.mkdirs()
            val zipFileEntries = zip.entries()

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                val entry = zipFileEntries.nextElement() as ZipEntry
                val currentEntry = entry.name

                val destFile = File(extractFolder, currentEntry)
                //destFile = new File(newPath, destFile.getName());
                val destinationParent = destFile.parentFile

                // create the parent directory structure if needed
                destinationParent.mkdirs()

                if (!entry.isDirectory) {
                    val `is` = BufferedInputStream(zip.getInputStream(entry))
                    // establish buffer for writing file
                    val data = ByteArray(2048)

                    // write the current file to disk
                    val fos = FileOutputStream(destFile)
                    val dest = BufferedOutputStream(fos, 2048)

                    // read and write until last byte is encountered
                    var currentByte = `is`.read(data, 0, 2048)
                    while (currentByte != -1) {
                        dest.write(data, 0, currentByte)
                        currentByte = `is`.read(data, 0, 2048)
                    }
                    dest.flush()
                    dest.close()
                    `is`.close()
                }
            }
        } catch (e: Exception) {
            print("Zipping error" + e.toString())
        }
        return extractFolder
    }
}