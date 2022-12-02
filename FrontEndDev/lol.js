fetch(
  "/myServlet3?topTenTracks=" +
    topTenTracks +
    "&topTenArtists=" +
    topTenArtists +
    "&topFiveGenres=" +
    topFiveGenres,
  { method: "GET" }
).then((response) => {
  const json = response.json();
  // do whatever with json
});
