package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentAdapter
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentClickListener
import com.example.kmitlcompanion.ui.mapboxview.adapter.OnButtonClickListener
import com.example.kmitlcompanion.ui.mapboxview.adapter.OnMenuClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject


class ViewComment @Inject constructor(
    private val commentAdapter: CommentAdapter
){

    //private lateinit var bottomSheetDialog: BottomSheetDialog

    fun setup(viewModel: MapboxViewModel, recyclerView: RecyclerView, context: Context,alertBuilder : AlertDialog.Builder) {

        //???
        val commentClickListener = CommentClickListener { author, message, level, parentId ->
            val id = (viewModel.commentList.value?.size ?: 0) + 1
            viewModel.addComment(Comment(
                id,
                "15/05/2021 at 12:30",
                author,
                message,
                0,
                0,
                false,false,
                true
            ))
        }
        commentAdapter.commentClickListener = commentClickListener

        //bottomSheetDialog = BottomSheetDialog(context)

        commentAdapter.setOnLikeClickListener(object : OnButtonClickListener{
            override fun onButtonClick(comment: Comment, position: Int) {
                var newComment = Comment(
                    id = comment.id,
                    date = comment.date,
                    author = comment.author,
                    message = comment.message,
                    like = comment.like + (if(comment.isLikedComment) -1 else 1),
                    dislike = comment.dislike + (if(comment.isDisLikedComment) -1 else 0),
                    isLikedComment = !comment.isLikedComment,
                    isDisLikedComment = false,
                    myComment = comment.myComment
                )
                viewModel.likeDisLikeCommentUpdate(newComment, position)
                //Log.d("test_like", comment.toString())
            }
        })

        commentAdapter.setOnDisLikeClickListener(object : OnButtonClickListener{
            override fun onButtonClick(comment: Comment, position: Int) {
                var newComment = Comment(
                    id = comment.id,
                    date = comment.date,
                    author = comment.author,
                    message = comment.message,
                    like = comment.like + (if(comment.isLikedComment) -1 else 0),
                    dislike = comment.dislike + (if(comment.isDisLikedComment) -1 else 1),
                    isLikedComment = false,
                    isDisLikedComment = !comment.isDisLikedComment,
                    myComment = comment.myComment
                )
                viewModel.likeDisLikeCommentUpdate(newComment, position)
                //Log.d("test_disLike", comment.toString())
            }
        })

        commentAdapter.setOnMenuClickListener(object : OnMenuClickListener{
            override fun onMenuClick(comment: Comment, bottomSheetDialog: BottomSheetDialog) {
                //Log.d("test_menu", comment.toString())
                bottomSheetDialog.setCancelable(true)
                bottomSheetDialog.show()

            }

            override fun onEditClick(comment: Comment,position: Int,bottomSheetDialog: BottomSheetDialog) {
                bottomSheetDialog.dismiss()
                viewModel.editCommentForm(comment,position)
                //Log.d("test_edit",comment.id.toString())
            }

            override fun onDeleteClick(comment: Comment,position: Int,bottomSheetDialog: BottomSheetDialog
            ) {
                bottomSheetDialog.dismiss()

                alertBuilder.setTitle("ลบความคิดเห็นของคุณ")
                alertBuilder.setMessage("คุณต้องการลบความคิดเห็นอย่างถาวรหรือไม่ ?")
                alertBuilder.setNegativeButton("ไม่",null)
                alertBuilder.setPositiveButton("ใช่",DialogInterface.OnClickListener { dialogInterface, i ->
                    //Log.d("test_deleteListener",comment.id.toString())
                    viewModel.deleteCommentUpdate(comment.id.toString(),position)
                })
                alertBuilder.show()

                //Log.d("test_delete",comment.id.toString())
            }
        })

        recyclerView.adapter = commentAdapter

    }

    fun update(commentList : MutableList<Comment>) {
        commentAdapter.submitList(commentList)
    }

    fun onAlert(title : String,message : String,positive : String,negative : String, dialogInterfaceListener: DialogInterface.OnClickListener ){

    }

}