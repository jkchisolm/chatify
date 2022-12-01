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
    "http://localhost:8080/test-stuff/myForm?user=" +
      username +
      "&pass=" +
      password,
    { method: "GET", mode: "*cors" }
  );
  console.log(response);
  alert("Check console");
  // const responseText = await response.text();
  // alert(responseText);

  // alert(
  //   "Logged in user (placeholder). Will redirect to login page 1 seconds after clicking OK."
  // );
  // setTimeout(() => {
  //   alert("Redirecting to spotify page to log in user");
  //   requestAuthorization();
  //   // window.location.href = "countdown.html";
  // }, 1000);
};
