const handleLoginRedirect = () => {
  // get params from url
  const params = new URL(document.location).searchParams;
  document.createElement("p").innerHTML = params;
  // return params;
};

const onIndexPageLoad = () => {
  if (window.location.search.length > 0) {
    handleLoginRedirect();
  } else {
    // idk
  }
};

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
