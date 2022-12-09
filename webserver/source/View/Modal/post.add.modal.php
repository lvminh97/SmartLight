<div class="modal fade" id="addPostModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel" style="font-weight: bold;">Thêm bài đăng mới</h5>
				<button class="close" type="button" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="addPostForm">
					<div class="form-group">
						<label for="post_title">Tiêu đề</label>
						<input type="text" class="form-control" id="post_title" style="width: 100%;">
					</div>
					<div class="form-group">
						<label for="post_room">Chọn phòng</label>
                        <select class="form-control" id="post_room">
                        <?php 
						foreach($viewParams['roomList'] as $room){ ?>
							<option value="<?php echo $room['room_id'] ?>"><?php echo $room['name']." - ".getFullAddress($room) ?></option>
						<?php
						} ?>
                        </select>
					</div>
                    <div class="form-group">
                        <label for="post_content">Nội dung bài đăng</label>
                        <script>
                            tinymce.init({
                                selector: "#post_content",
                                height: 350,
                                theme: 'silver',
                                plugins: 'lists table',
                                toolbar: 'undo redo | link image | code | alignleft aligncenter alignright alignjustify | bold italic underline | numlist bullist',
                                table_appearance_options: true,
                            });
                        </script>
                        <textarea id="post_content"></textarea>
                    </div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-secondary" data-dismiss="modal">Bỏ qua</button>
				<button class="btn btn-primary" data-dismiss="modal" onclick="addPost()">Đăng</button>
			</div>
		</div>
	</div>
</div>