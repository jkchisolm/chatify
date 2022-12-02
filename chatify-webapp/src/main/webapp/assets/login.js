const loginUser = async () => {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  // verify that user has entered something
  if (username === "" || password === "") {
    alert("Please enter a username and password");
    return;
  }
  // try making GET request to servlet with username and password?
  // if successful, return response in an alert

  //fetch
  const response = await fetch(
    "/chatify-webapp/LoginServlet?user=" +
      username +
      "&pass=" +
      password,
    { method: "GET" }
  );
  const json = await response.json();
  console.log(json.Worked);
  if (json.Worked == "false") {
    alert("Incorrect username or password");
  } else {
    // Store username as cookie
    document.cookie = "username=" + json.Username;
    // Re-auth user through Spotify
    requestAuthorization();
    // window.location.href = "countDown.html";
  }
};
