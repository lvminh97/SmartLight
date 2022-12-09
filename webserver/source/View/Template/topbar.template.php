<div class="row" style="padding-top: 10px; padding-bottom: 10px; background-image: url(assets/img/bgt.png); background-size: cover;">
  <div class="col-md-4">
    <img src="./assets/img/logo.png" style="width: auto; height: 100px;">
  </div>
  <?php 
  if($viewParams['user'] === null) { ?>
  <div class="col-md-5"></div>
  <div class="col-md-3 row">
    <div class="col-md-6">
      <a href="./?site=signup"><i class="fa fa-user-plus"></i> Đăng ký</a>
    </div>
    <div class="col-md-6">
      <a href="./?site=login"><i class="fa fa-sign-in-alt"></i> Đăng nhập</a>
    </div>
  </div>
  <?php
  }
  else { ?>
  <div class="col-md-4"></div>
  <div class="col-md-4 row">
    <div class="col-md-9">
      Xin chào bạn <a href="#"><?php echo $viewParams['user']['fullname'] ?></a>
    </div>
    <div class="col-md-3">
      <a href="./?action=logout">Đăng xuất</a>
    </div>
  </div>
  <?php 
  } ?>
</div>