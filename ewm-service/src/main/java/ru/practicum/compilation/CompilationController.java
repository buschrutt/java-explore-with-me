package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationWithEventsDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.error.ewmException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationWithEventsDto> findCompilations(@RequestParam(value = "pinned", defaultValue = "false") Boolean pinned,
                                                 @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return compilationService.findCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationWithEventsDto findCompilationById(@PathVariable Integer compId) throws ewmException {
        return compilationService.findCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationWithEventsDto addCompilation(@RequestBody @Valid CompilationDto compilationDto) throws ewmException {
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) throws ewmException {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationWithEventsDto updateCompilation(@PathVariable Integer compId,
                                            @RequestBody CompilationDto compilationDto) throws ewmException {
        return compilationService.updateCompilation(compId, compilationDto);
    }
}
