<?php
?>
<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <link rel="icon" href="View/image/logo_icon.png" type="image/x-icon">
   <title>Đăng ký</title>

   <!-- custom css file link  -->
   <link rel="stylesheet" href="View/Css/style.css">

</head>
<body>
   
<div class="form-container">

   <form action="/?action=signup" method="post">
      <h3>Đăng ký ngay</h3>
      <?php
      if(isset($viewParams['error'])){
         foreach($viewParams['error'] as $error){
            echo '<span class="error-msg">'.$error.'</span>';
         };
      }
      ?>
      <input type="hidden" name="platform" value="web">
      <input type="text" id="fullname" name="fullname" required placeholder="Nhập tên của bạn">
      <input type="email" id="email" name="email" required placeholder="Nhập email của bạn">
      <input type="text" id="mobile" name="mobile" required placeholder="Nhập số điện thoại của bạn">
      <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu">
      <input type="password" id="cpassword" name="cpassword" required placeholder="Xác nhận mật khẩu">
      <input type="submit" id="signupBtn" name="submit" value="Đăng ký" class="form-btn">
      <p>Bạn đã có sẵn tài khoản? <a href="/?site=login"> Đăng nhập ngay</a></p>
   </form>
</div>

</body>
</html>