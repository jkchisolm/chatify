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
