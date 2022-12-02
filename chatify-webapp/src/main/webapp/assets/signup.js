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
      "/test-stuff/SignupServlet?user=" +
      username +
      "&pass=" +
      password +
      "&pass2=" + confirmPassword,
      { method: "GET" }
  );
  const json = await response.json();
  console.log(json);
  // alert("Registered user (placeholder)");
};
