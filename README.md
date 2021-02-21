# HLS로 간단한 music streaming 구현해보기

> 이 프로젝트는 2년전쯤 스트리밍 서비스에 관심이 생겨서 간단한 예제로 구현해보았던 것인데, 관심을 가지고 조사하고 구현해보았던 것이 아까워 기록으로 남기고자 한다.

> 음원이나 영상을 다운로드받아서 소비하던 시대가 있었다. 인터넷 속도가 지금보다 느리던 시절이라 용량이 큰 영화나 미드를 다운받아보려면 몇시간이 걸리기도했다. 많은 서비스들이 스트리밍으로 서비스 방식을 바꾸게 된 데에는 스트리밍으로 끊김없이 고화질 영상을 볼수있게된 인터넷 속도와 기술의 발전도 있겠지만, 소비자들이 점점 더 간편함과 즉시성을 좇아서라고 생각한다. 스트리밍 서비스는, 애써 전체 영상을 다운로드 해서 내 취향이 맞는지 아닌지 뽑기 해야하는 기회 비용 없이 OTT에서 제공하는 컨텐츠들을 한번의 클릭만으로 바로 고화질로 이용할수 있고, 실시간 라이브 방송을 지연없이 시청할수 있는게 가장 큰 장점이다. 

> 그러면, 스트리밍 서비스는 어떻게 고화질의 영화나 라이브방송을 끊김없이 제공 할 수 있는걸까?의 물음에서 시작으로 HLS를 알게되었다.


전통적인 방식의 스트리밍은 RTSP(Real-Time Streaming Protocol)/RTP(Real-time Transport Protocol)를 이용했다. 이 방식은 1)스트리밍을 위한 별도의 서버를 구축하여야 하고 2)프로토콜 자체가 어려웠으며 3)HTTP 처럼 well-known port가 아니기 때문에 방화벽을 사용하고 있는 클라이언트 환경에서는 서비스가 원활하지 않은 등의 단점이 있었다.


HLS(HTTP Live Streaming)는 HTTP 기반 Streaming 프로토콜로 Apple에 의해 개발되었다. HTTP 프로토콜을 이용하므로 별도의 서버를 구축할 필요없다.

HLS 방식에서는 하나의 원본 동영상을 여러개의 ts파일로 나눈다. ts파일들의 재생순서는 m3u8이라는 메타 파일에 저장된다. 서버는 클라이언트가 HTTP로 동영상 파일을 요청하면 고용량 파일을 잘게 나누어 클라이언트에 전달하므로, 클라이언트는 재생할 차례가 되지않은 데이터를 받느라고 네트워크에 오버헤드를 겪을 필요가 없다.

adaptive HLS 방식은 네트워크 환경이 불안정한 경우에도 클라이언트에게 끊김없는 서비스를 제공해줄수있다. adaptive HLS는 동영상 화질별로 여러버전의 m3u8과 ts파일을 생성한다. 서버는 클라이언트의 네트워크 환경에 따라 동적으로 고화질 혹은 저화질의 ts파일을 제공한다. 스트리밍 서비스를 이용할때 영상화질이 갑자기 떨어져 보이다가 좋아지는 경험을 해본적이 있을텐데, 이런 이유때문이다.

### 서버 구현
내가 좋아하는 노래 두개를 aac 파일로 다운받았다. (aac 확장자는 mp3 다음 버전 오디오 코덱으로 mp3보다 높은 음질의 스펙을 갖췄다) FFmpeg 라는 미디어 크로스 플랫폼을 이용해 aac파일을 m3u8과 ts 파일로 생성했다. HLS 프로토콜 컨셉을 테스트해보고자 하는게 이번 개발의 의 핵심이므로 별도의 서버를 구축하지않고 AWS의 CloudFront를 이용했다. CloudFront에 생성한 m3u8과 ts 파일들을 업로드하고 외부로부터 접속 가능함을 확인했다.

### 클라이언트 구현
Android의 ExoPlayer에서 지원하는 HlsMediaSource를 이용하여 hls 서비스를 이용하도록 구현해보았다. 아마 내부적으로 m3u8 파일부터 가져와서 메타 정보를 참조하여 시간마다 정의된 ts파일들을 순차적으로 가져와서 재생하는 방식일것 같다.
```
                    uri = Uri.parse("http://xxxxx.cloudfront.net/Jerry/" + selSong + "/"+selSong+".m3u8");
                    Log.d("URI", "" + uri);
                    mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                    player.prepare(mediaSource);
                    player.setPlayWhenReady(true);
```

<img src="https://user-images.githubusercontent.com/62507373/108594803-65df1a80-73bf-11eb-9bbf-8ff88f42519f.png" width="300" height="550">

### Github repository
[music_streaming](https://github.com/jerry92k/music_streaming)

