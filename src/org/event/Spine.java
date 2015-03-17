// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * <h3>Represents stimuli with associated reactions</h3>
 * When a stimulus occurs, appropriate reactions are triggered
 * @author wolfram77
 */
public class Spine extends ConcurrentHashMap<String, Set<Reactable>> implements Runnable {
    
    // data
    String stimulus;
    Map args;
    
    // static data
    static Reactable fallback = new DefReaction();
    static ExecutorService threads = Executors.newCachedThreadPool((Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });
    
    // init code
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                threads.shutdown();
                try { threads.awaitTermination(3650, TimeUnit.DAYS); }
                catch(InterruptedException e) {}
            }
        });
    }
    
    
    /**
     * <h3>Convert a string from camel case to hyphen case</h3>
     * @param str camel case string
     * @return hyphen case string
     */
    String _toHyphenCase(String str) {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c >= 'A' && c<='Z') {
                if(i > 0) s.append('-');
                s.append((char)(c-'A'+'a'));
            }
            else s.append(c);
        }
        return s.toString();
    }
    
    
    /**
     * <h3>Add static / instance reaction-methods of a class</h3>
     * @param obj object containing reaction-methods (null if static)
     * @param cls class containing reaction-methods
     */
    private void _onClass(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String mthd = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!mthd.startsWith("on") || mthd.length()<=2 || (!isstatic && bestatic)) continue;
            // save appropriately
            String stim = _toHyphenCase(mthd.substring(2));
            Reaction reaction = bestatic? new Reaction(cls, mthd) : new Reaction(obj, mthd);
            if(m.isAnnotationPresent(Reacts.class)) reaction.speed(m.getAnnotation(Reacts.class).value());
            on(stim, reaction);
        }
    }
    
    
    /**
     * <h3>Initialize {@linkplain Reactable} set of stimulus</h3>
     * @param stimulus name of stimulus
     */
    void _initStimulus(String stimulus) {
        if(get(stimulus) != null) return;
        put(stimulus, Collections.newSetFromMap(new ConcurrentHashMap<Reactable, Boolean>()));
    }
    
    
    /**
     * <h3>Indicate a stimulus, causing reactions to trigger</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void _is(String stimulus, Map args) {
        Set<Reactable> reactions = get(stimulus);
        if(reactions == null) fallback.on(stimulus, args);
        else reactions.stream().forEach((e) -> {
            e.on(stimulus, args);
        });
    }
    
    
    /**
     * <h3>Set fallback reaction, for stimulus with no reaction</h3>
     * @param fallback fallback reaction
     */
    public static void fallback(Reactable fallback) {
        Spine.fallback = fallback;
    }
    
    
    /**
     * <h3>Get fallback reaction, for stimulus with no reaction</h3>
     * @return fallback reaction
     */
    public static Reactable fallback() {
        return fallback;
    }
    
    
    /**
     * <h3>Create an empty spine</h3>
     * Associate reactions to stimuli with {@code on(...)}
     */
    public Spine() {
    }
    
    
    /**
     * <h3>Create a spine from class</h3>
     * {@code on<stimulus>()} static methods are associated {@code <stimulus>} stimulus <br/>
     * Use {@code @Reacts("slow")} annotation to indicate slow reaction-methods
     * @param cls class containing reaction-methods
     */
    public Spine(Class cls) {
        _onClass(null, cls);
    }
    
    
    /**
     * <h3>Create a spine from object</h3>
     * {@code on<stimulus>()} methods are associated {@code <stimulus>} stimulus <br/>
     * Use {@code @Reacts("slow")} annotation to indicate slow reaction-methods
     * @param obj object containing reaction-methods
     */
    public Spine(Object obj) {
        _onClass(obj, obj.getClass());
    }
    
    
    /**
     * <h3>Tell spine speed ("fast" or "slow")</h3>
     * A fast spine triggers reactions synchronously <br/>
     * A slow spine triggers reactions asynchronously
     * @param speed speed of spine
     * @return spine for chaining
     */
    public Spine speed(String speed) {
        stimulus = speed.equalsIgnoreCase("slow")? "" : null;
        return this;
    }
    
    
    /**
     * <h3>Get spine speed ("fast" or "slow")</h3>
     * A fast spine triggers reactions synchronously <br/>
     * A slow spine triggers reactions asynchronously
     * @return speed of spine
     */
    public String speed() {
        return stimulus != null? "slow" : "fast";
    }
    
    
    /**
     * <h3>Indicate a stimulus asynchronously</h3>
     * DONT CALL THIS!
     */
    @Override
    public void run() {
        _is(stimulus, args);
    }
    
    
    /**
     * <h3>Indicate a stimulus, causing reactions to trigger</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Map args) {
        if(this.stimulus == null) { _is(stimulus, args); return this; }
        this.stimulus = stimulus;
        this.args = args;
        return this;
    }
    
    
    /**
     * <h3>Indicate a stimulus, causing reactions to trigger</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Object... args) {
        return is(stimulus, Coll.map(args));
    }
    
    
    /**
     * <h3>Set a reaction to trigger on a stimulus</h3>
     * @param stimulus name of stimulus
     * @param reaction reaction to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Reactable reaction) {
        _initStimulus(stimulus);
        get(stimulus).add(reaction);
        return this;
    }
    
    
    /**
     * <h3>Set reactions to trigger on a stimulus</h3>
     * @param stimulus name of stimulus
     * @param reactions reactions to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Collection<Reactable> reactions) {
        _initStimulus(stimulus);
        get(stimulus).addAll(reactions);
        return this;
    }
    
    
    /**
     * <h3>Set a reaction to trigger on stimuli</h3>
     * @param stimuli names of stimuli
     * @param reaction reaction to trigger
     * @return spine for chaining
     */
    public Spine on(Collection<String> stimuli, Reactable reaction) {
        stimuli.stream().forEach((stim) -> {
            on(stim, reaction);
        });
        return this;
    }
    
    
    /**
     * <h3>Set associated reactions to trigger on stimuli</h3>
     * @param assoc stimuli with associated reactions to trigger
     * @return spine for chaining
     */
    public Spine on(Map<String, Set<Reactable>> assoc) {
        assoc.keySet().stream().forEach((stim) -> {
            on(stim, assoc.get(stim));
        });
        return this;
    }
    
    
    /**
     * <h3>Turn off a reaction for a stimulus</h3>
     * @param stimulus name of stimulus
     * @param reaction reaction to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Reactable reaction) {
        Set<Reactable> s = get(stimulus);
        if(s != null) s.remove(reaction);
        return this;
    }
    
    
    /**
     * <h3>Turn off reactions for a stimulus</h3>
     * @param stimulus name of stimulus
     * @param reactions reactions to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Collection<Reactable> reactions) {
        Set<Reactable> e = get(stimulus);
        if(e != null) e.removeAll(reactions);
        return this;
    }
    
    
    /**
     * <h3>Turn off a reaction for stimuli</h3>
     * @param stimuli names of stimuli
     * @param reaction reaction to turn off
     * @return spine for chaining
     */
    public Spine off(Collection<String> stimuli, Reactable reaction) {
        stimuli.stream().forEach((stim) -> {
            off(stim, reaction);
        });
        return this;
    }
    
    
    /**
     * <h3>Turn off associated reactions for stimuli</h3>
     * @param map stimuli with associated reactions to turn off
     * @return spine for chaining
     */
    public Spine off(Map<String, Set<Reactable>> map) {
        map.keySet().stream().forEach((stim) -> {
            off(stim, map.get(stim));
        });
        return this;
    }
    
    
    /**
     * <h3>Turn off all reactions for a stimulus</h3>
     * @param stimulus name of stimulus
     * @return spine for chaining
     */
    public Spine off(String stimulus) {
        remove(stimulus);
        return this;
    }
    
    
    /**
     * <h3>Turn off all reactions for all stimuli</h3>
     * @return spine for chaining
     */
    public Spine off() {
        clear();
        return this;
    }
}
