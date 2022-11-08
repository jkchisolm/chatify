// document
//   .getElementsByClassName("submitButton")[0]
//   .addEventListener("click", () => {
//     let username = documents.getElementById(username).value;
//     let password = documents.getElementById(password).value;
//     let confirmPassword = documents.getElementById(confirmPassword).value;

//     // verify that password and confirm password are the same
//     if (password !== confirmPassword) {
//       alert("Passwords do not match");
//       return;
//     }
//     alert("Registered user (placeholder)");
//   });

const registerUser = () => {
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
  alert("Registered user (placeholder)");
};
