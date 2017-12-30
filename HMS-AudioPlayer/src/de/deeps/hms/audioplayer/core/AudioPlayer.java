package de.deeps.hms.audioplayer.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * @author Deeps
 */

public class AudioPlayer {

	private File dirFile;
	private Player player;
	private String audioFileDir;

	public AudioPlayer(String audioFileDir) throws IOException {
		this.audioFileDir = audioFileDir;
		dirFile = new File(audioFileDir);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			throw new IOException();
		}
	}

	public boolean playAudio(String audioFileName) {
		stopPlayingAudio();
		try {
			FileInputStream fis = new FileInputStream(
					new File(audioFileDir, audioFileName));
			player = new Player(fis);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						player.play();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			}).start();
			return true;
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> listAudioFiles() {
		List<String> fileNames = new LinkedList<>();
		for (File file : dirFile.listFiles()) {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}

	public void stopPlayingAudio() {
		if (player != null) {
			player.close();
		}
	}
}
