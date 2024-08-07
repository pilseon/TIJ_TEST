package com.example.Trip_In_Jeju.kategorie.attractions.controller;


import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.service.AttractionsService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attractions")
public class AttractionsController {
    private final AttractionsService attractionsService;
    private final RatingService ratingService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "subCategory", defaultValue = "all") String subCategory
    ) {
        Page<Attractions> paging = attractionsService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "attractions/list";
    }

    @GetMapping("/detail/{id}")
    public String getAttractionsDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Attractions attractions = attractionsService.getAttractionsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "attractions");
        double averageScore = ratingService.calculateAverageScore(id, "attractions");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        String nickname = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                nickname = ((UserDetails) principal).getUsername();
            } else {
                nickname = principal.toString();
            }
        }
        model.addAttribute("attractions", attractions);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("nickname", nickname);
        return "attractions/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Attractions attractions = attractionsService.getAttractionsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "attractions");
        double averageScore = ratingService.calculateAverageScore(id, "attractions");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        model.addAttribute("attractions", attractions);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        return "attractions/review";
    }

    @PostMapping("/review/{id}")
    public String submitRating(
            @PathVariable("id") Long id,
            @RequestParam("score") Integer score,
            @RequestParam(value = "ratingId", required = false) Long ratingId, // ratingId는 optional로 설정
            @RequestParam("comment") String comment,
            Authentication authentication,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/attractions/detail/" + id;
        }
        String nickname = ((UserDetails) authentication.getPrincipal()).getUsername();
        ratingService.saveRating(id, score, ratingId, comment, nickname, thumbnail, "attractions");
        return "redirect:/attractions/detail/" + id;
    }

    @PostMapping("/review/edit/{id}")
    public String updateRating(
            @PathVariable("id") Long id,
            @RequestParam("ratingId") Long ratingId,
            @RequestParam("score") Integer score,
            @RequestParam("comment") String comment
    ) {
        ratingService.updateRating(ratingId, score, comment);
        return "redirect:/attractions/detail/" + id;
    }

    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, @RequestParam("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/dessert/detail/" + id;
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/attractions/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Member> memberOptional = memberService.findByUsername(username);

        if (!memberOptional.isPresent()) {
            return "redirect:/attractions/detail/" + id + "?error=memberNotFound";
        }

        Member member = memberOptional.get();
        boolean liked = attractionsService.toggleLike(id, member);

        if (!liked) {
            return "redirect:/attractions/detail/" + id + "?error=alreadyLiked";
        }

        return "redirect:/attractions/detail/" + id;
    }
}
