/*
 * Copyright since 2018 Nhut Do <mr.nhut@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.donhut.web.util;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class.getName());
    private static final String FILE_CSV_CHARSET = StandardCharsets.UTF_8.name();

    private FileUtil() {
    }

    /**
     * Read CSV file to {@link List} of {@link T} objects.
     *
     * @param expectedClass Class use for deserialization.
     * @param csvFile Csv file.
     * @param csvSeparator Csv column separator.
     * @param <T> Class which will be deserialize into.
     * @return {@link List} of {@link T} objects. Each {@link T} represent one row.
     * @throws IOException File read fails.
     */
    public static <T> List<T> readCsvFile(
            final Class<T> expectedClass, final File csvFile, final char csvSeparator)
            throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(csvFile);
        return readCsvStreamData(expectedClass, csvFile.getAbsolutePath(), csvSeparator, fileInputStream);
    }

    public static <T> List<T> readCsvStreamData(
            final Class<T> expectedClass, final String csvFile, final char csvSeparator,
            final InputStream fileInputStream)
            throws IOException {
        LOG.info("Reading file '{}' to CSV object...", csvFile);
        // Using {@link BOMInputStream} we can ignore byte order marks by encoding.
        try (BOMInputStream bomInputStream =
                     new BOMInputStream(fileInputStream, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE,
                             ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE);
                 InputStreamReader inputStreamReader =
                         new InputStreamReader(bomInputStream, FILE_CSV_CHARSET)) {

            final CsvToBeanBuilder<T> builder = new CsvToBeanBuilder<T>(inputStreamReader)
                    .withType(expectedClass).withSeparator(csvSeparator);
            final List<T> objects = builder.build().parse();
            LOG.info("Total of {} items read from the file {} (csv separator: {}).",
                    objects.size(), csvFile, csvSeparator);
            return objects;
        }
    }
}
