package ui.data.stub

import entity.Note
import entity.NoteDraft
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

fun getExampleArchivedNote() = Note(
    id = 1,
    createdAt = Clock.System.now().minus(33.days),
    archivedAt = Clock.System.now().minus(4.hours),
    header = "Заголовок архивированной заметки",
    content = """
            Для разнообразия тут не такой уж и большой текст.
            Вполне умещается в две строчки, да.
        """.trimIndent()
)

fun getExampleArchivedNotes() =
    List(13) { getExampleArchivedNote() }

fun getExampleNewNote() =
    NoteDraft(
        header = "Очень и очень длинный заголовок, который не помещается целиком в контейнер",
        content = """
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque dignissim sed ipsum sit amet sodales. Aenean posuere, sem vel lobortis volutpat, quam urna finibus elit, nec mollis odio quam non leo. Suspendisse accumsan augue at urna congue porta. Nunc rhoncus erat non neque blandit, quis venenatis enim pellentesque. Quisque nec mauris nunc. Aenean eu faucibus ipsum, porta ornare massa. Ut mollis nibh in euismod tristique. Pellentesque magna elit, blandit sit amet vestibulum vitae, mollis vel ex. Sed iaculis porta ipsum, ut consequat sem faucibus et. Nullam pretium nisi vitae consequat vestibulum. Fusce consequat orci ut diam dictum ultricies.
                    
                    Donec nunc erat, pretium aliquam tortor vitae, euismod vulputate mauris. Aliquam sagittis, metus sed efficitur scelerisque, augue lacus facilisis nunc, vitae convallis velit felis a neque. Duis pharetra nibh dui, sed iaculis tortor scelerisque nec. Maecenas at pharetra ante. Maecenas luctus finibus libero sit amet pulvinar. Praesent facilisis in lacus ac faucibus. Aliquam nec felis interdum, molestie velit ac, fringilla nulla. Nulla facilisi. Sed risus ex, interdum ac vehicula eget, viverra ut odio. Cras sollicitudin cursus diam, quis euismod justo ultricies quis. Donec pretium ante et molestie dictum. Suspendisse condimentum eu erat non viverra.

                    Proin felis augue, suscipit sed finibus ac, feugiat quis metus. Nam at bibendum lectus. Vivamus sit amet congue justo. Aenean finibus volutpat elit, vehicula imperdiet nisl. Aliquam posuere pretium consectetur. Vivamus luctus sed nulla dapibus dictum. Nam volutpat vehicula nisl nec tempus. Nulla fermentum felis non convallis pulvinar. Etiam iaculis felis ut leo eleifend vehicula. Maecenas dapibus erat sit amet metus ornare scelerisque. Sed quis risus egestas, sodales neque et, molestie diam. Etiam at sollicitudin est. Morbi quis justo vel tortor malesuada consequat gravida id dolor. Cras condimentum odio tempus mauris aliquet, eu tincidunt sem euismod. Vivamus quis metus sit amet nunc porta consequat ac nec velit. Nullam suscipit neque eget felis dictum, eu lacinia mauris consectetur.

                    Mauris vel varius arcu. Praesent augue felis, consequat eget leo vitae, malesuada varius ligula. Integer interdum fringilla dui, nec consectetur justo convallis eget. Morbi molestie lorem nulla, vitae feugiat lectus ultrices sed. Nullam sed vestibulum lacus. Sed risus risus, vestibulum sit amet semper tincidunt, sollicitudin at arcu. Vestibulum dignissim aliquam magna in sagittis.

                    Cras eu massa at tortor porttitor vehicula. Nam a est aliquam, sodales nisi eget, imperdiet tellus. Donec molestie efficitur elit, ac euismod enim tristique non. Quisque ut nisi dictum, porta augue quis, egestas justo. Aenean vulputate lectus lacus, sit amet sollicitudin ante sagittis ac. Suspendisse sit amet mi risus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean semper at neque ut porta. Duis elementum sapien nunc, vel malesuada dolor pellentesque ut. Sed ac tortor dolor. Suspendisse potenti. Sed sed nisl dictum ante accumsan congue. Sed nec est ipsum. Donec mollis enim et massa malesuada, eget imperdiet neque aliquet.

                    Nam feugiat blandit tortor, quis vestibulum ante finibus eu. Proin non elit at velit fermentum condimentum. Aenean pulvinar blandit turpis, vitae cursus massa consectetur non. Cras pulvinar mi mollis, elementum dui a, vulputate sem. Maecenas ullamcorper nulla eget lorem placerat, et tristique odio sollicitudin. Mauris sodales dolor non mauris congue, nec blandit ante congue. Cras ut varius magna. Fusce consequat erat id ullamcorper blandit. Sed id pharetra ipsum. 
                """.trimIndent(),
    )

fun getExampleNote() =
    Note(
        id = 0,
        createdAt = Clock.System.now(),
        archivedAt = null,
        header = "Очень и очень длинный заголовок, который не помещается целиком в контейнер",
        content = """
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque dignissim sed ipsum sit amet sodales. Aenean posuere, sem vel lobortis volutpat, quam urna finibus elit, nec mollis odio quam non leo. Suspendisse accumsan augue at urna congue porta. Nunc rhoncus erat non neque blandit, quis venenatis enim pellentesque. Quisque nec mauris nunc. Aenean eu faucibus ipsum, porta ornare massa. Ut mollis nibh in euismod tristique. Pellentesque magna elit, blandit sit amet vestibulum vitae, mollis vel ex. Sed iaculis porta ipsum, ut consequat sem faucibus et. Nullam pretium nisi vitae consequat vestibulum. Fusce consequat orci ut diam dictum ultricies.
                    
                    Donec nunc erat, pretium aliquam tortor vitae, euismod vulputate mauris. Aliquam sagittis, metus sed efficitur scelerisque, augue lacus facilisis nunc, vitae convallis velit felis a neque. Duis pharetra nibh dui, sed iaculis tortor scelerisque nec. Maecenas at pharetra ante. Maecenas luctus finibus libero sit amet pulvinar. Praesent facilisis in lacus ac faucibus. Aliquam nec felis interdum, molestie velit ac, fringilla nulla. Nulla facilisi. Sed risus ex, interdum ac vehicula eget, viverra ut odio. Cras sollicitudin cursus diam, quis euismod justo ultricies quis. Donec pretium ante et molestie dictum. Suspendisse condimentum eu erat non viverra.

                    Proin felis augue, suscipit sed finibus ac, feugiat quis metus. Nam at bibendum lectus. Vivamus sit amet congue justo. Aenean finibus volutpat elit, vehicula imperdiet nisl. Aliquam posuere pretium consectetur. Vivamus luctus sed nulla dapibus dictum. Nam volutpat vehicula nisl nec tempus. Nulla fermentum felis non convallis pulvinar. Etiam iaculis felis ut leo eleifend vehicula. Maecenas dapibus erat sit amet metus ornare scelerisque. Sed quis risus egestas, sodales neque et, molestie diam. Etiam at sollicitudin est. Morbi quis justo vel tortor malesuada consequat gravida id dolor. Cras condimentum odio tempus mauris aliquet, eu tincidunt sem euismod. Vivamus quis metus sit amet nunc porta consequat ac nec velit. Nullam suscipit neque eget felis dictum, eu lacinia mauris consectetur.

                    Mauris vel varius arcu. Praesent augue felis, consequat eget leo vitae, malesuada varius ligula. Integer interdum fringilla dui, nec consectetur justo convallis eget. Morbi molestie lorem nulla, vitae feugiat lectus ultrices sed. Nullam sed vestibulum lacus. Sed risus risus, vestibulum sit amet semper tincidunt, sollicitudin at arcu. Vestibulum dignissim aliquam magna in sagittis.

                    Cras eu massa at tortor porttitor vehicula. Nam a est aliquam, sodales nisi eget, imperdiet tellus. Donec molestie efficitur elit, ac euismod enim tristique non. Quisque ut nisi dictum, porta augue quis, egestas justo. Aenean vulputate lectus lacus, sit amet sollicitudin ante sagittis ac. Suspendisse sit amet mi risus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean semper at neque ut porta. Duis elementum sapien nunc, vel malesuada dolor pellentesque ut. Sed ac tortor dolor. Suspendisse potenti. Sed sed nisl dictum ante accumsan congue. Sed nec est ipsum. Donec mollis enim et massa malesuada, eget imperdiet neque aliquet.

                    Nam feugiat blandit tortor, quis vestibulum ante finibus eu. Proin non elit at velit fermentum condimentum. Aenean pulvinar blandit turpis, vitae cursus massa consectetur non. Cras pulvinar mi mollis, elementum dui a, vulputate sem. Maecenas ullamcorper nulla eget lorem placerat, et tristique odio sollicitudin. Mauris sodales dolor non mauris congue, nec blandit ante congue. Cras ut varius magna. Fusce consequat erat id ullamcorper blandit. Sed id pharetra ipsum. 
                """.trimIndent(),
    )

fun getExampleNotes() =
    List(13) { getExampleNote() }