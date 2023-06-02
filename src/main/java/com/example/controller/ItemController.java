package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Category;
import com.example.entity.Item;
import com.example.form.ItemForm;
import com.example.service.CategoryService;
import com.example.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	private final CategoryService categoryService;
	private final ItemService itemService;
	@Autowired
    public ItemController(ItemService itemService,CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }
    // 商品一覧の表示
    @GetMapping
    public String index(Model model) {
    	// データの疎通確認
    	List<Item> items = this.itemService.findByDeletedAtIsNull();
        // コンソールよりListの中身を確認する
        model.addAttribute("items", items);
        return "item/index";
    }

    // 商品登録ページ表示用
    @GetMapping("toroku")
    public String torokuPage(@ModelAttribute("itemForm") ItemForm itemForm, Model model) {
        // 処理を追加
    	 // Categoryモデルから一覧を取得する
        List<Category> categories = this.categoryService.findAll();

        // viewにカテゴリを渡す
        model.addAttribute("categories", categories);
        return "item/torokuPage";
    }

    // 商品登録の実行
    @PostMapping("toroku")
    public String toroku(ItemForm itemForm) {
    	this.itemService.save(itemForm);
        // 一覧ページへリダイレクトします
        return "redirect:/item";
    }

    // 商品編集ページ
    @GetMapping("henshu/{id}")
    public String henshuPage(@PathVariable("id") Integer id, Model model
                             , @ModelAttribute("itemForm") ItemForm itemForm) {
    	// Entityクラスのインスタンスをidより検索し取得します
        Item item = this.itemService.findById(id);
        // フィールドのセットを行います
        itemForm.setName(item.getName());
        itemForm.setPrice(item.getPrice());
        itemForm.setCategoryId(item.getCategoryId());
        List<Category> categories = this.categoryService.findAll();
        // idをセットします
        model.addAttribute("id", id);
        model.addAttribute("categories", categories);
        // templates/item/henshuPageを表示します
        return "item/henshuPage";
    }

    // 商品編集の実行
    @PostMapping("henshu/{id}")
    public String henshu(@PathVariable("id") Integer id, @ModelAttribute("itemForm") ItemForm itemForm) {
        // 処理を追加
    	this.itemService.update(id, itemForm);
        return "redirect:/item";
    }

    // 商品削除の実行
    @PostMapping("sakujo/{id}")
    public String sakujo(@PathVariable("id") Integer id) {
        // 処理を追加
    	this.itemService.delete(id);
        return "redirect:/item";
    }

}
