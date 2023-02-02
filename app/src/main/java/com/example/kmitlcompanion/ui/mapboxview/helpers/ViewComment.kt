package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentAdapter
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentClickListener
import com.example.kmitlcompanion.ui.mapboxview.adapter.OnButtonClickListener
import com.example.kmitlcompanion.ui.mapboxview.adapter.OnMenuClickListener
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewComment @Inject constructor(
    private val commentAdapter: CommentAdapter,
    private val dateUtils: DateUtils
){

    //private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var weakCommentView : WeakReference<EditText>
    private lateinit var weakSendCommentBtn : WeakReference<Button>
    private lateinit var weakEditCommentBtn : WeakReference<Button>
    private lateinit var weakCancelEditCommentBtn : WeakReference<Button>

    private lateinit var viewModel : MapboxViewModel

    fun setup(viewModel: MapboxViewModel, comment : EditText, sendCommentBtn: Button, editCommentBtn: Button
              ,cancelEditCommentBtn: Button,recyclerView: RecyclerView,alertBuilder : AlertDialog.Builder) {

        this.weakCommentView = WeakReference(comment)
        this.weakSendCommentBtn = WeakReference(sendCommentBtn)
        this.weakEditCommentBtn = WeakReference(editCommentBtn)
        this.weakCancelEditCommentBtn = WeakReference(cancelEditCommentBtn)

        this.viewModel = viewModel

        setupButtonListener()

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
            }
        })

        commentAdapter.setOnMenuClickListener(object : OnMenuClickListener{
            override fun onMenuClick(comment: Comment, bottomSheetDialog: BottomSheetDialog) {
                bottomSheetDialog.setCancelable(true)
                bottomSheetDialog.show()

            }

            override fun onEditClick(comment: Comment,position: Int,bottomSheetDialog: BottomSheetDialog) {
                bottomSheetDialog.dismiss()
                viewModel.editCommentForm(comment,position)
            }

            override fun onDeleteClick(comment: Comment,position: Int,bottomSheetDialog: BottomSheetDialog
            ) {
                bottomSheetDialog.dismiss()

                alertBuilder.setTitle("ลบความคิดเห็นของคุณ")
                alertBuilder.setMessage("คุณต้องการลบความคิดเห็นอย่างถาวรหรือไม่ ?")
                alertBuilder.setNegativeButton("ไม่",null)
                alertBuilder.setPositiveButton("ใช่",DialogInterface.OnClickListener { dialogInterface, i ->
                    viewModel.deleteCommentUpdate(comment.id.toString(),position)
                })
                alertBuilder.show()
            }
        })

        recyclerView.adapter = commentAdapter
    }

    fun update(commentList : MutableList<Comment>) {
        commentAdapter.submitList(commentList)
    }

    private fun setupButtonListener(){
        commend?.doAfterTextChanged {
            var checkValid = (it.toString().replace(Regex("[\\s\\n]+"), "") != "")
            sendCommend?.isEnabled = checkValid
            editCommentBtn?.isEnabled = checkValid
        }

        sendCommend?.setOnClickListener {
            //val commentId = (viewModel.commentList.value?.size ?: 0) + 1
            this.viewModel.addComment(Comment(
                0,
                dateUtils.shinGetTime(),
                "",
                commend?.text.toString(),
                0,
                0,
                isLikedComment = false,
                isDisLikedComment = false,
                myComment = true
            ))
            commend?.text?.clear()
        }

        editCommentBtn?.setOnClickListener {
            viewModel.editCommentUpdate(commend?.text.toString())
            editCommentBtn?.visibility = View.GONE
            cancelEditCommentBtn?.visibility = View.GONE
            sendCommend?.visibility = View.VISIBLE
            commend?.text?.clear()
            commend?.clearFocus()
        }

        cancelEditCommentBtn?.setOnClickListener {
            editCommentBtn?.visibility = View.GONE
            cancelEditCommentBtn?.visibility = View.GONE
            sendCommend?.visibility = View.VISIBLE
            commend?.text?.clear()
            commend?.clearFocus()
        }
    }

    fun onAlert(title : String,message : String,positive : String,negative : String, dialogInterfaceListener: DialogInterface.OnClickListener ){

    }

    private val commend
        get() = this.weakCommentView.get()

    private val sendCommend
        get() = weakSendCommentBtn.get()

    private val editCommentBtn
        get() = weakEditCommentBtn.get()

    private val cancelEditCommentBtn
        get() = weakCancelEditCommentBtn.get()

}