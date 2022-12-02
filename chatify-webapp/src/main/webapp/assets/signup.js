const registerUser = async () => {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;
  let confirmPassword = document.getElementById("confirmPassword").value;

  // verify that user has entered something
  if (username === "" || password === "" || confirmPassword === "") {
    alert("Please enter a username and password");
    return;
  }

  // verify that password and confirm password are the same
  if (password !== confirmPassword) {
    alert("Passwords do not match");
    return;
  }

  const response = await fetch(
      "/chatify-webapp/SignupServlet?user=" +
      username +
      "&pass=" +
      password +
      "&pass2=" + confirmPassword,
      { method: "GET" }
  );
  const json = await response.json();
  if (json.status == 1) {
    document.cookie = "username=" + json.username;
    window.location.href = "countDown.html";
  } else if (json.status == -1) {
    alert("Username already exists");
  } else if (json.status == -2) {
    alert("Password must be 8 characters");
  } else if (json.status == -3) {
    alert("Username must be at least 4 characters");
  }
};
