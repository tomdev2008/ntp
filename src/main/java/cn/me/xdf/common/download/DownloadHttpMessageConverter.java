package cn.me.xdf.common.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

/**
 * 下载监听
 * 
 * @author xiaobin
 * 
 */
public class DownloadHttpMessageConverter extends
		AbstractHttpMessageConverter<DownloadHelper> {

	@Override
	protected DownloadHelper readInternal(Class<? extends DownloadHelper> arg0,
			HttpInputMessage arg1) throws IOException,
			HttpMessageNotReadableException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return DownloadHelper.class.equals(clazz);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void writeInternal(DownloadHelper downFile,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		overrideHeader(outputMessage.getHeaders(), downFile.getHeaders());
		File f = downFile.getFile();
		if (f != null) {
			FileCopyUtils.copy(new FileInputStream(f), outputMessage.getBody());
			if (downFile.isClearFile()) {
				f.delete();
			}
		} else {
			if (downFile.getCharset() == null) {
				// Charset.forName("UTF-8")
				FileCopyUtils.copy(
						downFile.getContent(),
						new OutputStreamWriter(outputMessage.getBody(), Charset
								.forName("UTF-8")));
			} else {
				FileCopyUtils.copy(downFile.getContent(),
						new OutputStreamWriter(outputMessage.getBody(),
								downFile.getCharset()));
			}
		}

	}

	private void overrideHeader(HttpHeaders headers, Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (headers.containsKey(key) && value != null) {
				headers.remove(key);
			}
			headers.add(key, value);
		}
	}

}