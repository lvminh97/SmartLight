<div class="row my-menu">
    <div class="my-menu-item">
        <a href="./.">TRANG CHỦ</a>
    </div>
    <div class="my-menu-item">
        <a href="./?site=room_list">DANH SÁCH PHÒNG TRỌ</a>
    </div>
    <?php 
    if($viewParams['user'] !== null && $viewParams['user']['role'] == "Role_Tenant"){ ?>
    <div class="my-menu-item">
        <a href="./?site=my_room">PHÒNG CỦA TÔI</a>
    </div>
    <?php 
    } ?>
    <?php 
    if($viewParams['user'] !== null && $viewParams['user']['role'] == "Role_Host"){ ?>
    <div class="my-menu-item">
        <a href="./?link=manage-home">DÀNH CHO CHỦ TRỌ</a>
    </div>
    <?php
    } ?>
    <?php 
    if($viewParams['user'] !== null && $viewParams['user']['role'] == "Role_Admin"){ ?>
    <div class="my-menu-item">
        <a href="./?link=approve-post">DÀNH CHO ADMIN</a>
    </div>
    <?php
    } ?>
</div>