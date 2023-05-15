<!DOCTYPE html>
<html lang="en">
<head>
   <link rel="icon" href="View/image/logo_icon.png" type="image/x-icon">
   <title>Đăng nhập</title>

   <!-- custom css file link  -->
   <link rel="stylesheet" href="View/Css/style.css">

</head>
<body>
   
<div class="form-container">

   <form action="/?action=login" method="post">
      <h3>Đăng nhập</h3>
      <?php
      if(isset($viewParams['error'])){
         foreach($viewParams['error'] as $error){
            echo '<span class="error-msg">'.$error.'</span>';
         };
      }
      ?>
      <input type="hidden" name="platform" value="web">
      <?php $username = isset($viewParams["username"]) ? $viewParams["username"] : ""; ?>
      <input type="email" name="username" required placeholder="Nhập email của bạn" value="<?php echo $username ?>">
      <input type="password" name="password" required placeholder="Nhập mật khẩu của bạn">
      <input type="submit" name="submit" value="đăng nhập" class="form-btn">
      <p>Bạn chưa có tài khoản? <a href="/?site=register"> Đăng ký ngay</a></p>
   </form>

</div>

</body>
</html>