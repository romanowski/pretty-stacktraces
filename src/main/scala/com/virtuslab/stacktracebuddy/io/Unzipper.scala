package com.virtuslab.stacktraces.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import java.util.Map.entry

object Unzipper:

  def unzipFile(file: File): File =
    val destDir = Files.createTempDirectory(file.getName + "_sources");
    val fileInputStream = FileInputStream(file)
    val input = ZipInputStream(fileInputStream)
    var ze = input.getNextEntry
    while (ze != null)
      val fileName = ze.getName
      val newFilePath = Paths.get(destDir.toAbsolutePath.toString, fileName)
      if (ze.isDirectory) then
        newFilePath.toFile.mkdirs
      else
        val file = newFilePath.toFile
        file.getParentFile.mkdirs
        val fos = FileOutputStream(file)
        input.transferTo(fos)
        fos.close()
      input.closeEntry
      ze = input.getNextEntry
    input.closeEntry
    input.close
    fileInputStream.close
    destDir.toFile
