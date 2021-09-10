package Ex02;


import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaServiceImpl implements MediaService{
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	Button btnPlay, btnPause, btnStop;
	boolean endOfMedia;

	Label labelTime;
	Slider sliderVolume;
	ProgressBar progressBar;
	ProgressIndicator progressIndicator;
	
	@Override
	public void myStart() {
		mediaPlayer.play();
	}
	@Override
	public void myStop() {
		mediaPlayer.stop();

	}
	@Override
	public void myPause() {
		mediaPlayer.pause();
	}
	public void setControl(Parent root) {
		mediaView = (MediaView)root.lookup("#fxMediaView");
		btnPlay = (Button)root.lookup("#btnPlay");
		btnPause = (Button)root.lookup("#btnPause");
		btnStop = (Button)root.lookup("#btnStop");

		labelTime = (Label)root.lookup("#labelTime");
		sliderVolume = (Slider)root.lookup("#sliderVolume");
		progressBar = (ProgressBar)root.lookup("#progressBar");
		progressIndicator = (ProgressIndicator)root.lookup("#progressIndicator");
	}
	public void setMedia(Parent root, String mediaName) {
		setControl(root);
		//System.out.println( getClass().getResource(mediaName) );//확인용
		Media media = new Media( getClass().getResource(mediaName).toString() );
		mediaPlayer = new MediaPlayer(media);

		mediaView.setMediaPlayer(mediaPlayer);

		mediaPlayer.setOnReady(new Runnable() {
			public void run() {
				btnPlay.setDisable(false);
				btnStop.setDefaultButton(true);
				btnPause.setDefaultButton(true);

				mediaPlayer.currentTimeProperty().addListener((a,b,c)->{
					// 흐르는 시간 / 최종 시간
					double progress = 
							mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
					progressBar.setProgress(progress);
					progressIndicator.setProgress(progress);
					labelTime.setText(
							(int)mediaPlayer.getCurrentTime().toSeconds() +
							"/"+
							(int)mediaPlayer.getTotalDuration().toSeconds()
							);
				});
			}
		});

		mediaPlayer.setOnPlaying(()->{
			sliderVolume.setValue(50.0);

			btnPlay.setDisable(true);
			btnStop.setDisable(false);
			btnPause.setDisable(false);
		});

		mediaPlayer.setOnPaused(()->{
			btnPlay.setDisable(false);
			btnStop.setDisable(false);
			btnPause.setDisable(true);
		});

		mediaPlayer.setOnEndOfMedia(()->{
			btnPlay.setDisable(false);
			btnStop.setDisable(true);
			btnPause.setDisable(true);
			myStop();
		});

	}
	public void volumeControl() {
		mediaPlayer.setVolume(sliderVolume.getValue() / 100.0);
	}
}