package com.danqiu.online.edu.retrofit;

import com.danqiu.online.edu.utils.L;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
/**
 * 带进度 下载请求体
 */
public class XProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private XDownloadProgress downloadListener;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public XProgressResponseBody(ResponseBody responseBody, XDownloadProgress downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                L.e("test", "-----read: " + (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (downloadListener!=null) {
                    if (bytesRead != -1) {
                        downloadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()),responseBody.contentLength());
                    }
                }
                return bytesRead;
            }
        };
    }
}
