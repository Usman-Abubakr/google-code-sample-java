package com.google;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  private String currentPlaying = "";
  private String currentPlayingId = "";
  private String previousPlaying = "";
  private Boolean pauseVideo = false;


  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
//    System.out.println("showAllVideos needs implementation"); // done
    System.out.println("Here's a list of all available videos: ");

    // Create new list to display sorted array
    ArrayList<String> listOfVideos = new ArrayList<String>();

    // Loop through all elements and create new string to be displayed
    for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
      String currentTag = videoLibrary.getVideos().get(i).getTags().toString();
      String newTag = currentTag.replace(",", "");

      listOfVideos.add(videoLibrary.getVideos().get(i).getTitle() + " (" + videoLibrary.getVideos().get(i).getVideoId() + ") " + newTag);
    }

    // Sort elements in array
    Collections.sort(listOfVideos, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.compareTo(s2);
      }
    });

    // Display videos with correct formatting
    for (int i = 0; i < listOfVideos.size(); i++) {
      System.out.println("  " + listOfVideos.get(i) + "\r");
    }
  }

  public void playVideo(String videoId) {
//    System.out.println("playVideo needs implementation"); // done
    currentPlaying = "";
    String searchedVideo = "";
    pauseVideo = false;

    // Check if valid video
    try {
      searchedVideo = videoLibrary.getVideo(videoId).getTitle();
      currentPlayingId = videoLibrary.getVideo(videoId).getVideoId();
    }
    catch(Exception e) {
      System.out.println("Cannot play video: Video does not exist");
    }

    // Valid video replaces current, which moves to previous
    if (!searchedVideo.isEmpty()) {
      currentPlaying = searchedVideo;

      if (!previousPlaying.isEmpty()) {
        System.out.println("Stopping video: " + previousPlaying);
      }
      previousPlaying = currentPlaying;

      System.out.println("Playing video: " + currentPlaying);
    }
  }

  public void stopVideo() {
//    System.out.println("stopVideo needs implementation"); // done

    // Check valid and empty
    if (!currentPlaying.isEmpty()) {
      System.out.println("Stopping video: " + currentPlaying);
      currentPlaying = "";
      currentPlayingId = "";
    }
    else {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
//    System.out.println("playRandomVideo needs implementation"); // done
    Random rand = new Random();

    // Obtain a number within number of videos.
    int n = rand.nextInt(videoLibrary.getVideos().size());

    // Get video id and send request to play method
    String toPlay;
    toPlay = videoLibrary.getVideos().get(n).getVideoId();
    currentPlayingId = toPlay;
    playVideo(toPlay);
  }

  public void pauseVideo() {
//    System.out.println("pauseVideo needs implementation"); // done

    // Check if video is available and send pause signal
    if (!currentPlaying.isEmpty()) {
      if (!pauseVideo) {
        pauseVideo = true;
        System.out.println("Pausing video: " + currentPlaying);
      }
      else {
        System.out.println("Video already paused: " + currentPlaying);
      }
    }
    else {
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
//    System.out.println("continueVideo needs implementation"); // done

    // Check if video is available and send un-pause signal
    if (!currentPlaying.isEmpty()) {
      if (pauseVideo) {
        pauseVideo = false;
        System.out.println("Continuing video: " + currentPlaying);
      }
      else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
    else {
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
//    System.out.println("showPlaying needs implementation"); // done

    // Check if video is available and retrieve data
    if (!currentPlaying.isEmpty()) {
      String currentTag = videoLibrary.getVideo(currentPlayingId).getTags().toString();
      String newTag = currentTag.replace(",", "");
      String toPrint = ("Currently playing: " + videoLibrary.getVideo(currentPlayingId).getTitle() + " (" + videoLibrary.getVideo(currentPlayingId).getVideoId() + ") " + newTag);

      if (!pauseVideo) {
        System.out.println(toPrint);
      }
      else {
        System.out.println(toPrint + " - PAUSED");
      }
    }
    else {
      System.out.println("No video is currently playing");
    }
  }




  /**
   * PART 2
   */



  // Create map for video play list
  Map<String, VideoPlaylist> playListMap = new HashMap<>();

  public void createPlaylist(String playlistName) {
//    System.out.println("createPlaylist needs implementation"); // done

    // Check if play list already exists without case sensitivity
    String lowerCaseName = playlistName.toLowerCase();
    if (playListMap.containsKey(lowerCaseName)) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    } else {
      playListMap.put(lowerCaseName, new VideoPlaylist(playlistName));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
//    System.out.println("addVideoToPlaylist needs implementation"); done

    String lowerCaseName = playlistName.toLowerCase();
    if (playListMap.containsKey(lowerCaseName)) {
      Video video = videoLibrary.getVideo(videoId);
      VideoPlaylist playList = playListMap.get(lowerCaseName);
      if (video != null) {
        if (playList.addVideo(video)) {
          System.out.printf("Added video to %s: %s%n", playlistName, video.getTitle());
        } else {
          System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
        }
      }
      else {
        System.out.printf("Cannot add video to %s: Video does not exist%n", playlistName);
      }
    }
    else {
      System.out.printf("Cannot add video to %s: Playlist does not exist%n", playlistName);
    }
  }

  public void showAllPlaylists() {
//    System.out.println("showAllPlaylists needs implementation"); // done

    List<String> lowerCaseNames = new ArrayList<>(playListMap.keySet());
    lowerCaseNames.sort(CharSequence::compare);
    if (lowerCaseNames.isEmpty()) {
      System.out.println("No playlists exist yet");
    }
    else {
      System.out.println("Showing all playlists:");
      lowerCaseNames.forEach(n -> System.out.println("  " + playListMap.get(n).name));
    }
  }

  public void showPlaylist(String playlistName) {
//    System.out.println("showPlaylist needs implementation"); // done

    VideoPlaylist playList = playListMap.get(playlistName.toLowerCase());
    if (playList != null) {
      System.out.printf("Showing playlist: %s%n", playlistName);
      if (playList.videos.isEmpty()) {
        System.out.println("  No videos here yet");
      } else {
        playList.videos.forEach(
                v -> System.out.println("  " + String.format("%s (%s) [%s]", v.getTitle(), v.getVideoId(), v.getTags().stream().reduce((t, s) -> t + " " + s).orElse("")))
        );
      }
    } else {
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n", playlistName);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
//    System.out.println("removeFromPlaylist needs implementation"); //done

    String lowerCaseName = playlistName.toLowerCase();
    VideoPlaylist playList = playListMap.get(lowerCaseName);
    if (playList != null) {
      Video video = videoLibrary.getVideo(videoId);
      if (video != null) {
        if (playList.removeVideo(video)) {
          System.out.printf("Removed video from %s: %s%n", playlistName, video.getTitle());
        }
        else {
          System.out.printf("Cannot remove video from %s: Video is not in playlist%n", playlistName);
        }
      }
      else {
        System.out.printf("Cannot remove video from %s: Video does not exist%n", playlistName);
      }
    }
    else {
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n", playlistName);
    }
  }

  public void clearPlaylist(String playlistName) {
//    System.out.println("clearPlaylist needs implementation"); // done

    String lowerCaseName = playlistName.toLowerCase();
    VideoPlaylist playList = playListMap.get(lowerCaseName);
    if (playList != null) {
      playList.videos = new ArrayList<>();
      System.out.println("Successfully removed all videos from " + playlistName);
    }
    else {
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n", playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
//    System.out.println("deletePlaylist needs implementation"); // done

    String lowerCaseName = playlistName.toLowerCase();
    if (playListMap.containsKey(lowerCaseName)) {
      playListMap.remove(lowerCaseName);
      System.out.println("Deleted playlist: " + playlistName);
    }
    else {
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n", playlistName);
    }
  }



  /**
   * PART 3
   */


  public void searchVideos(String searchTerm) {
//    System.out.println("searchVideos needs implementation"); // done

    searchVideosBy(x -> x.getTitle().toLowerCase().contains(searchTerm.toLowerCase()), searchTerm);

  }

  public void searchVideosWithTag(String videoTag) {
//    System.out.println("searchVideosWithTag needs implementation"); // done

    searchVideosBy(x -> x.getTags().stream()
            .anyMatch(t -> t.toLowerCase().contains(videoTag.toLowerCase())), videoTag);

  }


  private void searchVideosBy(Predicate<Video> function, String searchString) {
    List<Video> videos = videoLibrary.getVideos().stream()
            .filter(function)
            .sorted(Comparator.comparing(Video::getTitle))
            .collect(Collectors.toList());
    if (videos.isEmpty()) {
      System.out.println("No search results for " + searchString);
    } else {
      System.out.printf("Here are the results for %s:%n", searchString);
      AtomicInteger number = new AtomicInteger();
      videos.forEach(v -> {
        number.getAndIncrement();
        System.out.println(number + ") " + String.format("%s (%s) [%s]", v.getTitle(), v.getVideoId(), v.getTags().stream().reduce((t, s) -> t + " " + s).orElse(""))

        );
      });
      System.out.println("Would you like to play any of the above? "
              + "If yes, specify the number of the video.\n"
              + "If your answer is not a valid number, we will assume it's a no.");
      int answer;
      try {
        answer = new Scanner(System.in).nextInt() - 1;
      } catch (InputMismatchException ignored) {
        answer = -1;
      }
      if (answer >= 0 && answer < videos.size()) {
        playVideo(videos.get(answer).getVideoId());
      }
    }
  }





  /**
   * PART 4
   */


  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}