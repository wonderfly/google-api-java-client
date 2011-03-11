/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Concrete implementation of {@link AbstractInputStreamContent} that simply handles the transfer of
 * data from an input stream to an output stream. This should only be used for streams that can not
 * be re-opened and retried. If you have a stream that it is possible to recreate please create a
 * new subclass of {@link AbstractInputStreamContent}.
 * <p>
 * The {@link #type} field is required. The input stream is guaranteed to be closed at the end of
 * {@link #writeTo(OutputStream)}.
 * <p>
 * Sample use with a URL:
 *
 * <pre>
 * <code>
  private static void setRequestJpegContent(HttpRequest request, URL jpegUrl) throws IOException {
    InputStreamContent content = new InputStreamContent();
    content.inputStream = jpegUrl.openStream();
    content.type = "image/jpeg";
    request.content = content;
  }
 * </code>
 * </pre>
 *
 * @since 1.0
 * @author Yaniv Inbar
 */
public final class InputStreamContent extends AbstractInputStreamContent {

  /** Content length or less than zero if not known. Defaults to {@code -1}. */
  public long length = -1;

  /** Required input stream to read from. */
  public InputStream inputStream;

  /**
   * Sets the {@link #inputStream} from a file input stream based on the given file, and the
   * {@link #length} based on the file's length.
   * <p>
   * Sample use:
   *
   * <pre>
   * <code>
  private static void setRequestJpegContent(HttpRequest request, File jpegFile)
      throws FileNotFoundException {
    InputStreamContent content = new InputStreamContent();
    content.setFileInput(jpegFile);
    content.type = "image/jpeg";
    request.content = content;
  }
   * </code>
   * </pre>
   *
   * @deprecated Scheduled for removal in 1.5. Please use {@link FileContent} instead.
   */
  @Deprecated
  public void setFileInput(File file) throws FileNotFoundException {
    inputStream = new FileInputStream(file);
    length = file.length();
  }

  /**
   * Sets the {@link #inputStream} and {@link #length} from the given byte array.
   * <p>
   * For string input, call the appropriate {@link String#getBytes} method.
   * <p>
   * Sample use:
   *
   * <pre>
   * <code>
  static void setRequestJsonContent(HttpRequest request, String json) {
    InputStreamContent content = new InputStreamContent();
    content.setByteArrayInput(Strings.toBytesUtf8(json));
    content.type = "application/json";
    request.content = content;
  }
   * </code>
   * </pre>
   *
   * @deprecated Scheduled for removal in 1.5. Please use {@link ByteArrayContent} instead.
   */
  @Deprecated
  public void setByteArrayInput(byte[] content) {
    inputStream = new ByteArrayInputStream(content);
    length = content.length;
  }

  public long getLength() {
    return length;
  }

  public boolean retrySupported() {
    return false;
  }

  @Override
  protected InputStream getInputStream() {
    return inputStream;
  }

  /**
   * Writes the content provided by the given source input stream into the given destination output
   * stream.
   * <p>
   * The input stream is guaranteed to be closed at the end of the method.
   * </p>
   * <p>
   * Sample use:
   *
   * <pre><code>
    static void downloadMedia(HttpResponse response, File file)
        throws IOException {
      FileOutputStream out = new FileOutputStream(file);
      try {
        InputStreamContent.copy(response.getContent(), out);
      } finally {
        out.close();
      }
     }
     * </code></pre>
   * </p>
   *
   * @param inputStream source input stream
   * @param outputStream destination output stream
   * @throws IOException I/O exception
   *
   * @deprecated Scheduled for removal in version 1.5. Please use
   *             {@link AbstractInputStreamContent#copy} as an alternative.
   */
  @Deprecated
  public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
    AbstractInputStreamContent.copy(inputStream, outputStream);
  }
}
