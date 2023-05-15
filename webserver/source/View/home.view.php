<!DOCTYPE html>
<html lang="en">
<head>
   <link rel="icon" href="View/image/logo_icon.png" type="image/x-icon">
   <title>Trang chủ</title>
   <!-- custom css file link  -->
   <link rel="stylesheet" href="View/Css/style.css">
</head>
<body>

    <div class="home_page"> 
        <div class="navbar">
            <div class="row">
                <div class="icon">
                    <img class="logo-icon" src="View/image/logo_icon.png"><br>
                    <h2 class="logo"> SMART LIGHT </h2>
                </div>
                <div class="hello-user">
                    Xin chào <b><?php echo $_SESSION["smlgt_user"]["name"] ?></b><a href="/?action=logout" style="margin-left: 20px"> Đăng xuất</a>
                </div>
            </div>
        
            <div class="menu">
                <ul>
                    <li><a href="./.">TRANG CHỦ</a></li>
                    <li><a href="/?site=about">VỀ CHÚNG TÔI</a></li>
                    <li><a href="/?site=service">DỊCH VỤ</a></li>
                    <li><a href="/?site=light_design">HỆ THỐNG ĐÈN THÔNG MINH</a></li>
                    <li><a href="/?site=contact">LIÊN HỆ</a></li>
                </ul>
            </div>
        </div>
        <div class="devide"></div>
        <div class="content">
            <br><h1>Hệ thống chiếu sáng thông minh</h1>
            <div class="span-65">Đèn thông minh Indoor</div> 
            <!-- <button class="cn">
                <a href="#">Tham gia cùng chúng tôi</a> -->
            </button>
                
                <!-- <div class="form">
                    <h2>Login Here</h2>
                    <input type="email" name="email" placeholder="Enter Email Here">
                    <input type="password" name="" placeholder="Enter Password Here">
                    <button class="btnn"><a href="#">Login</a></button>

                    <p class="link">Don't have an account<br>
                    <a href="#">Sign up </a> here</a></p>
                    <p class="liw">Log in with</p>

                    <div class="icons">
                        <a href="#"><ion-icon name="logo-facebook"></ion-icon></a>
                        <a href="#"><ion-icon name="logo-instagram"></ion-icon></a>
                        <a href="#"><ion-icon name="logo-twitter"></ion-icon></a>
                        <a href="#"><ion-icon name="logo-google"></ion-icon></a>
                        <a href="#"><ion-icon name="logo-skype"></ion-icon></a>
                    </div>

                </div> -->
        </div>
    </div>
    <script src="https://unpkg.com/ionicons@5.4.0/dist/ionicons.js"></script>
</body>
</html>